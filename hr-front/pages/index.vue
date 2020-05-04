<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="info">
      <b-navbar-brand href="#">
        Human Resources
      </b-navbar-brand>

      <b-navbar-toggle target="nav-collapse" />
    </b-navbar>

    <b-container class="bv-example-row">
      <b-row>
        <b-col>
          <h1>Department</h1>
          <b-button variant="info" class="mb-2 float-right" @click="department(null, 'Create Department', $event.target)">
            New Department
          </b-button>
          <div>
            <b-table hover :items="depts" :fields="fields_depts">
              <template v-slot:cell(name)="row">
                {{ row.value }}
              </template>
              <template v-slot:cell(actions)="row">
                <b-button size="sm" variant="info" class="mr-1" @click="department(row.item, 'Update Department', $event.target)">
                  <b-icon icon="pencil-square" />
                </b-button>
                <b-button size="sm" variant="danger" class="mr-1" @click="deleteDepartment(row.item.id)">
                  <b-icon icon="trash2-fill" />
                </b-button>
              </template>
            </b-table>
          </div>
        </b-col>
        <b-col>
          <h1>Employee</h1>
          <b-button variant="info" class="mb-2 float-right" @click="employee(null, 'Create Employee', $event.target)">
            New Employee
          </b-button>
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
                  <b-icon icon="pencil-square" />
                </b-button>
                <b-button size="sm" variant="danger" class="mr-1" @click="deleteEmployee(row.item.id)">
                  <b-icon icon="trash2-fill" />
                </b-button>
              </template>
            </b-table>
          </div>
        </b-col>
      </b-row>
      <b-modal :id="employeeModal.id" :title="employeeModal.title" hide-footer @hide="resetEmployeeModal">
        <form ref="employeeModal">
          <b-form-group label="Name" label-for="name-input" >
            <b-form-input id="name-input" v-model="formEmployee.name"/>
          </b-form-group>
          <b-form-group label="Department" label-for="department-input">
            <b-form-select id="department-input" v-model="formEmployee.deptId">
              <b-form-select-option :value="null">
                Please select an option
              </b-form-select-option>
              <b-form-select-option v-for="dept in depts" :key="dept.id" :value="dept.id">
                {{ dept.name }}
              </b-form-select-option>
            </b-form-select>
          </b-form-group>
          <b-button v-if="!isUpdatedMode" type="submit" variant="primary" @click="createEmployee()">
            Create
          </b-button>
          <b-button v-if="isUpdatedMode" type="submit" variant="primary" @click="updateEmployee()">
            Update
          </b-button>
          <b-button type="reset" variant="danger">
            Reset
          </b-button>
        </form>
      </b-modal>
      <b-modal :id="departmentModal.id" :title="departmentModal.title" hide-footer @hide="resetDepartmentModal">
        <form ref="departmentModal">
          <b-form-group label="Name" label-for="name-input">
            <b-form-input id="name-input" v-model="formDepartment.name"/>
          </b-form-group>
          <b-button v-if="!isUpdatedMode" type="submit" variant="primary" @click="createDepartment()">
            Create
          </b-button>
          <b-button v-if="isUpdatedMode" type="submit" variant="primary" @click="updateDepartment()">
            Update
          </b-button>
          <b-button type="reset" variant="danger">
            Reset
          </b-button>
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
  async asyncData ({ $axios, env }) {
    const depts = await $axios.$get(env.API_URL + '/department')
    const empls = await $axios.$get(env.API_URL + '/employee')
    return { depts, empls }
  },
  data () {
    return {
      isUpdatedMode: false,
      fields_empls: [
        { key: 'id', label: 'ID' },
        { key: 'name', label: 'Full Name' },
        { key: 'deptId', label: 'Deptartment' },
        { key: 'actions', label: 'Actions' }
      ],
      fields_depts: [
        { key: 'id', label: 'ID' },
        { key: 'name', label: 'Full Name' },
        { key: 'actions', label: 'Actions' }
      ],
      formEmployee: {
        id: null,
        name: '',
        deptId: null
      },
      formDepartment: {
        id: null,
        name: ''
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
    async loadPage () {
      this.depts = await this.$axios.$get(process.env.API_URL + '/department')
      this.empls = await this.$axios.$get(process.env.API_URL + '/employee')
    },
    createEmployee () {
      this.$axios({
        url: process.env.API_URL + '/employee',
        method: 'post',
        headers: { 'Content-Type': 'application/json' },
        data: {
          name: this.formEmployee.name,
          deptId: this.formEmployee.deptId
        }
      })
      .then(function (response) {
        if (response.status === 200) {
          this.$refs['formEmployee'].hide()
          this.loadPage()
        } else {
          console.log('Some error')
        }
      })
      .catch(function (response) {
        console.log(response)
      })
    },
    updateEmployee () {
      this.$axios({
        url: process.env.API_URL + `/hr/employee/${this.formEmployee.id}/assign/${this.formEmployee.deptId}`,
        method: 'PUT'
      })
      .then((res) => {
        if (res.status === 200) {
          this.$refs['formEmployee'].hide()
          this.loadPage()
        } else {
          console.log('Some error')
        }
      })
      .catch((err) => {
        console.log(err)
      })
    },
    createDepartment () {
      this.$axios({
        url: process.env.API_URL + '/department',
        method: 'post',
        headers: { 'Content-Type': 'application/json' },
        data: {
          name: this.formDepartment.name
        }
      })
      .then(function (response) {
        if (response.status === 200) {
          this.$refs['formDepartment'].hide()
          this.loadPage()
        } else {
          console.log('Some error')
        }
      })
      .catch(function (response) {
        console.log(response)
      })
    },
    updateDepartment () {
      this.$axios({
        url: process.env.API_URL + '/department',
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        data: {
          id: this.formDepartment.id,
          name: this.formDepartment.name
        }
      })
      .then((res) => {
        if (res.status === 200) {
          this.$refs['formDepartment'].hide()
          this.loadPage()
        } else {
          console.log('Some error')
        }
      })
      .catch((err) => {
        console.log(err)
      })
    },
    employee (item, index, button) {
      this.isUpdatedMode = false
      this.employeeModal.title = `${index}`
      if (item && item.id) {
        this.formEmployee.id = item.id
        this.formEmployee.name = item.name
        this.formEmployee.deptId = item.deptId
        this.isUpdatedMode = true
      }
      this.$root.$emit('bv::show::modal', this.employeeModal.id, button)
    },
    department (item, index, button) {
      this.isUpdatedMode = false
      this.departmentModal.title = `${index}`
      if (item && item.id) {
        this.formDepartment.id = item.id
        this.formDepartment.name = item.name
        this.isUpdatedMode = true
      }
      this.$root.$emit('bv::show::modal', this.departmentModal.id, button)
    },
    resetEmployeeModal () {
      this.employeeModal.title = ''
      this.employeeModal.content = ''
    },
    resetDepartmentModal () {
      this.departmentModal.title = ''
      this.departmentModal.content = ''
    },
    async deleteEmployee (id) {
      const res = await this.$axios({
        url: `${process.env.API_URL}/employee/${id}`,
        method: 'delete',
        timeout: 8000
      })
      if (res.status === 200) {
        const res2 = await this.$axios({
          url: process.env.API_URL + '/employee',
          method: 'get',
          timeout: 8000
        })
        if (res2.status === 200) {
          this.empls = res2.data
        }
      }
    },
    async deleteDepartment (id) {
      const res = await this.$axios({
        url: `${process.env.API_URL}/department/${id}`,
        method: 'delete',
        timeout: 8000
      })
      if (res.status === 200) {
        const depts = await this.$axios({
          url: process.env.API_URL + '/department',
          method: 'get',
          timeout: 8000
        })
        if (depts.status === 200) {
          this.depts = depts.data
        }
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
