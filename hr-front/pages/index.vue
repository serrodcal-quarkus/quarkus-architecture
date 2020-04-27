<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="info">
      <b-navbar-brand href="#">Human Resources</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    </b-navbar>

    <b-container class="bv-example-row">
      <b-row>
        <b-col>
          <h1>Department</h1>
          <b-button variant="info" class="mb-2 float-right">New Department</b-button>
          <div>
            <b-table hover :items="depts" :fields="['name']">
              <template v-slot:cell(name)="row">
                {{ row.value }}
              </template>
            </b-table>
          </div>
        </b-col>
        <b-col>
          <h1>Employee</h1>
          <b-button variant="info" class="mb-2 float-right">New Employee</b-button>
          <div>
            <b-table hover :items="emplsParsed" :fields="['name','dept']">
              <template v-slot:cell(name)="row">
                <!--pre>{{row}}</pre-->
                {{ row.value }}
              </template>
              <template v-slot:cell(dept)="row">
                {{ row.value }}
              </template>
            </b-table>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
    async asyncData({ $axios }) {
      const depts = await $axios.$get('http://localhost:8080/api/v1/department')
      const empls = await $axios.$get('http://localhost:8080/api/v1/employee')
      const deptsParsed = depts.reduce(function(d) {
        return { [d.id]: d.name };
      });
      const emplsParsed = empls.map(function(e) {
         e.dept = deptsParsed[e.deptId];
         return e;
      });
      return { depts, emplsParsed }
    },
    data() {
      return {}
    }
  }
</script>

<style>
.mb-2 {
  margin-bottom: 1 !important;
}
</style>
