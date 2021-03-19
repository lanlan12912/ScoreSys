<template>
    <Form ref="formInline" :model="formInline" :rules="ruleInline" inline>
        <FormItem prop="user">
            <Input type="text" v-model="formInline.user" placeholder="Username">
                <Icon type="ios-person-outline" slot="prepend"></Icon>
            </Input>
        </FormItem>
        <FormItem prop="password">
            <Input type="password" v-model="formInline.password" placeholder="Password">
                <Icon type="ios-lock-outline" slot="prepend"></Icon>
            </Input>
        </FormItem>
        <FormItem>
            <Button type="primary" @click="handleSubmit('formInline')">Signin</Button>
        </FormItem>
    </Form>
</template>
<script>
    export default {
      data () {
        return {
          formInline: {
            user: '',
            password: ''
          },
          ruleInline: {
            user: [
              { required: true, message: 'Please fill in the user name', trigger: 'blur' }
            ],
            password: [
              { required: true, message: 'Please fill in the password.', trigger: 'blur' },
              { type: 'string', min: 6, message: 'The password length cannot be less than 6 bits', trigger: 'blur' }
            ]
          }
        }
      },
      methods: {
        handleSubmit(name) {
          let param = {
            number:"1516515"
          };
          let number="1516515"
          this.$refs[name].validate((valid) => {
            if (valid) {
              this.$http.post('/hello/say',param).then(res => {
                console.info('后台返回的数据', res);
              }).catch(err => {

              });
            } else {
              this.$Message.error('Fail!');
            }
          });
        }
      }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>


</style>
