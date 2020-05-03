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
          <b-button variant="info" class="mb-2 float-right" @click="department(null, 'Create Department', $event.target)">New Department</b-button>
          <div>
            <b-table hover :items="depts" :fields="fields_depts">
              <template v-slot:cell(name)="row">
                {{ row.value }}
              </template>
              <template v-slot:cell(actions)="row">
                <b-button size="sm" variant="info" class="mr-1" @click="department(row.item, 'Update Department', $event.target)">
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
          <b-button variant="info" class="mb-2 float-right" @click="employee(null, 'Create Employee', $event.target)">New Employee</b-button>
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
                <b-button size="sm" variant="info" class="mr-1" @click="employee(row.item, 'Update Employee', $event.target)">
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
      <b-modal :id="employeeModal.id" :title="employeeModal.title" hide-footer @hide="resetEmployeeModal">
        <form ref="employeeModal"> <!-- @submit.stop.prevent="handleSubmit" -->
          <b-form-group
            label="Name"
            label-for="name-input"
            invalid-feedback="Name is required">
            <b-form-input
              id="name-input"
              v-model="formEmployee.name"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            label="Department"
            label-for="department-input"
            invalid-feedback="Name is required">
            <b-form-select
              id="department-input"
              v-model="formEmployee.deptId"
              required
            >
              <b-form-select-option :value="null">Please select an option</b-form-select-option>
              <b-form-select-option v-for="dept in depts" :value="dept.id">{{ dept.name }}</b-form-select-option>
            </b-form-select>
          </b-form-group>
          <b-button type="submit" variant="primary" v-if="!isUpdatedMode" @click="createEmployee()">Create</b-button>
          <b-button type="submit" variant="primary" v-if="isUpdatedMode" @click="updateEmployee()">Update</b-button>
          <b-button type="reset" variant="danger">Reset</b-button>
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
      const depts = await $axios.$get('http://localhost/api/v1/department')
      const empls = await $axios.$get('http://localhost/api/v1/employee')
      return { depts, empls }
    },
    data() {
      return {
        isUpdatedMode: false,
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
        formEmployee: {
          name: "",
          deptId: null
        },
        formDepartment: {
          name: ""
        },
        employeeModal: {
          id: 'employee-modal',
          title: '',
          content: ''
        },
        departmentModal: {
          id: 'department-modal',
          title: '',
          content: ''
        }
      }
    },
    methods: {
      loadPage() {
        this.depts = this.$axios.$get('http://localhost/api/v1/department')
        this.empls = this.$axios.$get('http://localhost/api/v1/employee')
      },
      createEmployee() {
        const res = this.$axios({
            url: 'http://localhost/api/v1/employee',
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            data: {
              name: this.formEmployee.name,
              deptId: this.formEmployee.deptId
            }
        })
        .then(function (response) {
           if (response.status === 200) {
             console.log("Create Success")
             loadPage()
           } else {
             console.log("a tomar por culo ya")
           }
         })
         .catch(function (response) {
           console.log(response);
           resolve();
         });
      },
      updateEmployee() {
        this.$axios({
            url: 'http://localhost/api/v1/employee',
            method: 'put',
            headers: {'Content-Type': 'application/json'},
            data: {
              id: this.formEmployee.id,
              name: this.formEmployee.name,
              deptId: this.formEmployee.deptId
            }
        })
         .then(res => {
            if (res.status == 200) {
              this.loadPage();
            }
         })
         .catch(err => {
             console.log(err);
          });
      },
      employee(item, index, button) {
        this.isUpdatedMode = false
        this.employeeModal.title = `${index}`
        if (item && item.id) {
          this.formEmployee.name = item.name
          this.formEmployee.deptId = item.deptId
          this.isUpdatedMode = true
        }
        this.$root.$emit('bv::show::modal', this.employeeModal.id, button)
      },
      department(item, index, button) {
        this.departmentModal.title = `${index}`
        this.departmentModal.content = JSON.stringify(item, null, 2)
        this.$root.$emit('bv::show::modal', this.departmentModal.id, button)
      },
      resetEmployeeModal() {
        this.employeeModal.title = ''
        this.employeeModal.content = ''
      },
      resetDepartmentModal() {
        this.departmentModal.title = ''
        this.departmentModal.content = ''
      },
      async deleteEmployee(id) {
        let res = await this.$axios({
            url: `http://localhost/api/v1/employee/${id}`,
            method: 'delete',
            timeout: 8000
        })
        if(res.status == 200){
          let res2 = await this.$axios({
              url: 'http://localhost/api/v1/employee',
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
            url: `http://localhost/api/v1/department/${id}`,
            method: 'delete',
            timeout: 8000
        })
        if(res.status == 200){
          let depts = await this.$axios({
              url: 'http://localhost/api/v1/department',
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
