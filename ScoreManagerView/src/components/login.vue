<template>
  <Row class="back">
    <Col>
        <Form ref="formInline" :model="formInline" :rules="ruleInline"  class="inputbox">
           <FormItem label-width= "5">
               <p class="tilte">综合测评管理系统</p>
            </FormItem>
            <FormItem prop="user" label-width= "5">
                <Input type="text" v-model="formInline.user" placeholder="账号">
                    <Icon type="ios-person-outline" slot="prepend"></Icon>
                </Input>
            </FormItem>
            <FormItem prop="password" label-width="5">
                <Input type="password" v-model="formInline.password" placeholder="密码">
                    <Icon type="ios-lock-outline" slot="prepend"></Icon>
                </Input>
            </FormItem>
            <FormItem class="login-no-bottom">
                
            </FormItem>
            <FormItem  class="login-no-bottom">
              <Row>
                <Col span="7">
                  <CheckboxGroup>
                    <Checkbox label="记住密码"></Checkbox>
                  </CheckboxGroup>
                </Col>
                <Col span="4" offset = "4">
                  <Button type="primary" @click="handleSubmit('formInline')">登录</Button>
                </Col>
                <Col span="4" offset = "4">
                  <Button type="primary" @click="reset('formInline')">重置</Button>
                </Col>
              </Row>
            </FormItem>
        </Form>
    </Col>
</Row>
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
              { required: true, message: '请输入账号', trigger: 'blur' }
            ],
            password: [
              { required: true, message: '请输入密码', trigger: 'blur' },
              { type: 'string', min: 6, message: '账号密码长度不能少于6位', trigger: 'blur' }
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
              this.$Message.error('登录失败');
            }
          });
        }
      }
    }
</script>

<style lang="less" scoped>
.back{
  //让div充满整个屏幕
  width: 100%;
  height: 100%;
  //设置背景色
 
  // background: linear-gradient(#30cfd0, #330867);
  // background: -webkit-linear-gradient(#30cfd0, #330867);
  // background: -o-linear-gradient(#30cfd0, #330867); 
  // background: -moz-linear-gradient(#30cfd0, #330867);
  background-image: linear-gradient(120deg, #e0c3fc 0%, #8ec5fc 100%);
  background-size: cover;
  -moz-background-size: cover;
  -webkit-background-size: cover;;
  
  //设置div里面的元素居中显示
  position: absolute;
  flex-direction: column;
	justify-content: center;
  display:flex;
  align-items: center;
 .inputbox{
    border: 2px solid rgb(38, 136, 120);
    width: 100%;
    height: 100%;
    padding: 20px;
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    .tilte{
      margin-top: 20px;
      font-size: 36px;
      font-weight: 900;
    }
    .login-no-bottom {
      margin-bottom: 10px;
    }
  }
}
</style>
