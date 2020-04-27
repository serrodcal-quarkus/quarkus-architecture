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
            <b-table striped hover :items="items_dept"></b-table>
          </div>
        </b-col>
        <b-col>
          <h1>Employee</h1>
          <b-button variant="info" class="mb-2 float-right">New Employee</b-button>
          <div>
            <b-table striped hover :items="items_empl"></b-table>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
    data() {
      return {
        items_dept: [
          { name: 'HR' },
          { name: 'IT' }
        ],
        items_empl: [
          { name: 'Lechowsky', dept: 'IT' },
          { name: 'Serrodcal', dept: 'IT' }
        ]
      }
    }
  }
</script>

<style>
.mb-2 {
  margin-bottom: 1 !important;
}
</style>
