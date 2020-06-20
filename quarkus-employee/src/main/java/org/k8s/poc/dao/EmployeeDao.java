package org.k8s.poc.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.k8s.poc.domain.Employee;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class EmployeeDao {

    private io.vertx.mutiny.pgclient.PgPool client;

    @Inject
    @ConfigProperty(name = "myapp.schema.create", defaultValue = "true")
    boolean schemaCreate;

    @PostConstruct
    void config() {
        if (schemaCreate) {
            initdb();
        }
    }

    private void initdb() {
        client.query("DROP TABLE IF EXISTS employees").execute()
                .flatMap(r -> client.query("CREATE TABLE employees (id SERIAL PRIMARY KEY, name TEXT NOT NULL, dept_id BIGINT)").execute())
                .flatMap(r -> client.query("INSERT INTO employees (name) VALUES ('Lechowsky')").execute())
                .flatMap(r -> client.query("INSERT INTO employees (name) VALUES ('Serrodcal')").execute())
                .await().indefinitely();
    }

    EmployeeDao (io.vertx.mutiny.pgclient.PgPool client) {
        this.client = client;
    }

    public Multi<Employee> findAll() {
        return client.query("SELECT id, name, dept_id FROM employees").execute()
                // Create a Multi from the set of rows:
                .onItem().produceMulti(set -> Multi.createFrom().items(() -> StreamSupport.stream(set.spliterator(), false)))
                // For each row create a fruit instance
                .onItem().apply(EmployeeDao::from);
    }

    public Uni<Employee> findById(Long id) {
        return client.preparedQuery("SELECT id, name, dept_id FROM employees WHERE id = $1").execute(Tuple.of(id))
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Multi<Employee> findByDept(Long depId) {
        return client.preparedQuery("SELECT id, name, dept_id FROM employees WHERE dept_id = $1").execute(Tuple.of(depId))
                .onItem().produceMulti(set -> Multi.createFrom().items(() -> StreamSupport.stream(set.spliterator(), false)))
                .onItem().apply(EmployeeDao::from);
    }

    public Uni<Long> save(String name, Long deptId) {
        return client.preparedQuery("INSERT INTO employees (name, dept_id) VALUES ($1, $2) RETURNING (id)").execute(Tuple.of(name, deptId))
                .onItem().apply(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public Uni<Boolean> update(Long id, String name, Long deptId) {
        return client.preparedQuery("UPDATE employees SET name = $1, dept_id = $3 WHERE id = $2").execute(Tuple.of(name, id, deptId))
                .onItem().apply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public Uni<Boolean> delete(Long id) {
        return client.preparedQuery("DELETE FROM employees WHERE id = $1").execute(Tuple.of(id))
                .onItem().apply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    private static Employee from(Row row) {
        return new Employee(row.getLong("id"), row.getString("name"), row.getLong("dept_id"));
    }

}
