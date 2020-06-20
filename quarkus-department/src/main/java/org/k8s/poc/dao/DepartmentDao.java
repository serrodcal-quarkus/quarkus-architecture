package org.k8s.poc.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.k8s.poc.domain.Department;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class DepartmentDao {

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
        client.query("DROP TABLE IF EXISTS departments").execute()
                .flatMap(r -> client.query("CREATE TABLE departments (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute())
                .flatMap(r -> client.query("INSERT INTO departments (name) VALUES ('IT')").execute())
                .flatMap(r -> client.query("INSERT INTO departments (name) VALUES ('HR')").execute())
                .await().indefinitely();
    }

    DepartmentDao(io.vertx.mutiny.pgclient.PgPool client) {
        this.client = client;
    }

    public Multi<Department> findAll() {
        return client.query("SELECT id, name FROM departments").execute()
                // Create a Multi from the set of rows:
                .onItem().produceMulti(set -> Multi.createFrom().items(() -> StreamSupport.stream(set.spliterator(), false)))
                // For each row create a fruit instance
                .onItem().apply(DepartmentDao::from);
    }

    public Uni<Department> findById(Long id) {
        return client.preparedQuery("SELECT id, name FROM departments WHERE id = $1").execute(Tuple.of(id))
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Long> save(String name) {
        return client.preparedQuery("INSERT INTO departments (name) VALUES ($1) RETURNING (id)").execute(Tuple.of(name))
                .onItem().apply(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public Uni<Boolean> update(Long id, String name) {
        return client.preparedQuery("UPDATE departments SET name = $1 WHERE id = $2").execute(Tuple.of(name, id))
                .onItem().apply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public Uni<Boolean> delete(Long id) {
        return client.preparedQuery("DELETE FROM departments WHERE id = $1").execute(Tuple.of(id))
                .onItem().apply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    private static Department from(Row row) {
        return new Department(row.getLong("id"), row.getString("name"));
    }

}
