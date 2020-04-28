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
            <b-table hover :items="depts" :fields="fields_depts">
              <template v-slot:cell(name)="row">
                {{ row.value }}
              </template>
              <template v-slot:cell(actions)="row">
                <b-button size="sm" variant="info" class="mr-1">
                  <b-icon icon="pencil-square"></b-icon>
                </b-button>
                <b-button size="sm" variant="danger" class="mr-1">
                  <b-icon icon="trash2-fill"></b-icon>
                </b-button>
              </template>
            </b-table>
          </div>
        </b-col>
        <b-col>
          <h1>Employee</h1>
          <b-button variant="info" class="mb-2 float-right">New Employee</b-button>
          <div>
            <b-table hover :items="emplsParsed" :fields="fields_empls">
              <template v-slot:cell(name)="row">
                <!--pre>{{row}}</pre-->
                {{ row.value }}
              </template>
              <template v-slot:cell(dept)="row">
                {{ row.value }}
              </template>
              <template v-slot:cell(actions)="row">
                <b-button size="sm" variant="info" class="mr-1" @click="info(row.item, row.index, $event.target)">
                  <b-icon icon="pencil-square"></b-icon>
                </b-button>
                <b-button size="sm" variant="danger" class="mr-1" @click="deleteEmployee(row.item.id)">
                  <b-icon icon="trash2-fill"></b-icon>
                </b-button>
              </template>
            </b-table>
          </div>
        </b-col>
      </b-row>
      <b-modal :id="formModal.id" :title="formModal.title" ok-only @hide="resetFormModal">
        <p>Prueba</p>
      </b-modal>
    </b-container>
  </div>
</template>

<script>
import Vue from 'vue'
import { BootstrapVue, BootstrapVueIcons } from 'bootstrap-vue'

Vue.use(BootstrapVue)
Vue.use(BootstrapVueIcons)

export default {
    async asyncData({ $axios }) {
      const depts = await $axios.$get('http://localhost:8080/api/v1/department')
      const empls = await $axios.$get('http://localhost:8080/api/v1/employee')
      const deptsParsed = depts.reduce(function(d) {
        return { [d.id]: d.name };
      });
      const emplsParsed = empls.map(function(e) {
         if (e.deptId) {
           e.dept = deptsParsed[e.deptId];
           return e;
         } else {
           e.dept = "Unassigned";
           return e;
         }
      });
      return { depts, emplsParsed }
    },
    data() {
      return {
        fields_empls: [
          { key: 'name', label: 'Full Name'},
          { key: 'dept', label: 'Deptartment'},
          { key: 'actions', label: 'Actions' }
        ],
        fields_depts: [
          { key: 'name', label: 'Full Name'},
          { key: 'actions', label: 'Actions' }
        ],
        formModal: {
          id: 'form-modal',
          title: '',
          content: ''
        }
      }
    },
    methods: {
      info(item, index, button) {
        this.formModal.title = `Row index: ${index}`
        this.formModal.content = JSON.stringify(item, null, 2)
        this.$root.$emit('bv::show::modal', this.formModal.id, button)
      },
      resetFormModal() {
        this.formModal.title = ''
        this.formModal.content = ''
      },
      async deleteEmployee(id) {
        const deleted = await this.$axios.$delete(`http://localhost:8080/api/v1/employee/${id}`)
        //debugger
        if (deleted.status == 200) {
          const empls = await this.$axios.$get('http://localhost:8080/api/v1/employee')
          this.empls = empls
        }
      }
    }
  }
</script>

<style>
.mb-2 {
  margin-bottom: 1 !important;
}
</style>
