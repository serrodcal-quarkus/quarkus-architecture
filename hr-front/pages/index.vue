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
          <b-button variant="info" class="mb-2 float-right" @click="create('Create Department', $event.target)">New Department</b-button>
          <div>
            <b-table hover :items="depts" :fields="fields_depts">
              <template v-slot:cell(name)="row">
                {{ row.value }}
              </template>
              <template v-slot:cell(actions)="row">
                <b-button size="sm" variant="info" class="mr-1" @click="update(row.item, 'Update Department', $event.target)">
                  <b-icon icon="pencil-square"></b-icon>
                </b-button>
                <b-button size="sm" variant="danger" class="mr-1" @click="deleteDepartment(row.item.id)">
                  <b-icon icon="trash2-fill"></b-icon>
                </b-button>
              </template>
            </b-table>
          </div>
        </b-col>
        <b-col>
          <h1>Employee</h1>
          <b-button variant="info" class="mb-2 float-right" @click="create('Create Employee', $event.target)">New Employee</b-button>
          <div>
            <b-table hover :items="empls" :fields="fields_empls">
              <template v-slot:cell(name)="row">
                <!--pre>{{row}}</pre-->
                {{ row.value }}
              </template>
              <template v-slot:cell(dept)="row">
                {{ row.value }}
              </template>
              <template v-slot:cell(actions)="row">
                <b-button size="sm" variant="info" class="mr-1" @click="update(row.item, 'Update Employee', $event.target)">
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
        <form ref="formModal"> <!-- @submit.stop.prevent="handleSubmit" -->
          <b-form-group
            :state="nameState"
            label="Name"
            label-for="name-input"
            invalid-feedback="Name is required">
            <b-form-input
              id="name-input"
              v-model="name"
              :state="nameState"
              required
            ></b-form-input>
          </b-form-group>
        </form>
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
      return { depts, empls }
    },
    data() {
      return {
        fields_empls: [
          { key: 'id', label: 'ID'},
          { key: 'name', label: 'Full Name'},
          { key: 'deptId', label: 'Deptartment'},
          { key: 'actions', label: 'Actions' }
        ],
        fields_depts: [
          { key: 'id', label: 'ID'},
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
      getEmployees(empls, depts) {
        const deptsParsed = depts.reduce(function(d) {
          return { [d.id]: d.name };
        });
        const emplsParsed = empls.map(function(e) {
           if (deptsParsed && e.deptId && deptsParsed[e.deptId]) {
             e.dept = deptsParsed[e.deptId];
             return e;
           } else {
             e.dept = "Unassigned";
             return e;
           }
        });
        return emplsParsed
      },
      create(index, button) {
        this.formModal.title = `${index}`
        this.$root.$emit('bv::show::modal', this.formModal.id, button)
      },
      update(item, index, button) {
        this.formModal.title = `${index}`
        this.formModal.content = JSON.stringify(item, null, 2)
        this.$root.$emit('bv::show::modal', this.formModal.id, button)
      },
      resetFormModal() {
        this.formModal.title = ''
        this.formModal.content = ''
      },
      async deleteEmployee(id) {
        let res = await this.$axios({
            url: `http://localhost:8080/api/v1/employee/${id}`,
            method: 'delete',
            timeout: 8000
        })
        if(res.status == 200){
          let res2 = await this.$axios({
              url: 'http://localhost:8080/api/v1/employee',
              method: 'get',
              timeout: 8000
          })
          if (res2.status == 200) {
            this.empls = res2.data
          }
        }
        return
      },
      async deleteDepartment(id) {
        let res = await this.$axios({
            url: `http://localhost:8080/api/v1/department/${id}`,
            method: 'delete',
            timeout: 8000
        })
        if(res.status == 200){
          let depts = await this.$axios({
              url: 'http://localhost:8080/api/v1/department',
              method: 'get',
              timeout: 8000
          })
          if (depts.status == 200) {
            this.depts = depts.data
          }
        }
        return
      }
    }
  }
</script>

<style>
.mb-2 {
  margin-bottom: 1 !important;
}
</style>
