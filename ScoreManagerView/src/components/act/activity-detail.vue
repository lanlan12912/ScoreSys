<template>
    <div>
        <Col span="16" class="actDetail">
            <Row>
                <Col span="11">
                    <div class="actInfo">
                        <Row><h2 :title="actInfo.actName">{{actInfo.actName}}</h2></Row>
                        <Row>
                            <Col span="10">
                                <p><b>获奖得分：</b>
                                    <p><b><img src="../../images/11.png">一等奖：</b>+4</p>
                                    <p><b><img src="../../images/22.png">二等奖：</b>+3</p>
                                    <p><b><img src="../../images/33.png">三等奖：</b>+2</p>
                                    <p><b><img src="../../images/33.png">其他获奖：</b>+1</p>
                                    <p><b><img src="../../images/33.png">参与得分：</b>+0.5</p>
                                </p>
                            </Col>
                            <Col span="14">
                                <p :title="actInfo.actRank"><b>活动级别：{{actInfo.actRank}}</b></p>
                                <p :title="actInfo.actHost"><b>主办方：{{actInfo.actHost}}</b></p>
                                <p :title="actInfo.actSite"><b>活动地点：{{actInfo.actSite}}</b></p>
                                <p :title="actInfo.startDate"><b>开始时间：{{actInfo.startDate}}</b></p>
                                <p :title="actInfo.endDate"><b>结束时间：{{actInfo.endDate}}</b></p>
                            </Col>
                        </Row>
                        <Row>
                            <Button class="btns" type="info" @click="compeleteImg()">完善材料</Button>
                            <Button class="btns" type="success">申报通过</Button>
                            <Button class="btns" type="warning">申报退回</Button>
                            <Button class="btns" type="success">我要报名</Button>
                            <Button class="btns" type="success">我要参与</Button>
                            <Button class="btns" type="info">审核材料</Button>
                        </Row>
                    </div>
                </Col>
                <Col span="13">
                    <div class="imgLunBo">
                        <Carousel loop>
                            <CarouselItem v-if="actImgs.length<=0">
                                <img src="../../images/nodata1.jpg" />
                            </CarouselItem>
                            <CarouselItem v-if="actImgs.length>0" v-for="(item,index) in actImgs" :key="index">
                                <img :src="item.src"/>
                            </CarouselItem>
                        </Carousel>
                    </div>
                </Col>
            </Row>
            <Row>
                <Col span="24">
                    <div class="actDesc">
                        <h3>活动详情：</h3>
                        <p>{{actInfo.actDesc}}</p>
                    </div>
                </Col>
            </Row>
        </Col>
        <Col span="7" >
            <div class="userRank">
                <Row class="rankRow">
                    <Col span="3"><Icon type="ios-thumbs-up" size="22"/></Col>
                    <Col span="5"><Icon type="ios-person" size="22"/></Col>
                    <Col span="5"><b>班级</b></Col>
                    <Col span="5"><b>姓名</b></Col>
                    <Col span="5"><b>得分</b></Col>
                </Row>
                <Row v-for="(item,index) in partUsers" :key="index"  class="rankRow" 
                :class="index==0?'firstRank':(index==1?'secondRank':(index==2?'thirdRank':'othorRank'))" >
                    <Col span="3">
                        <p v-if="index==0"><img src="../../images/1.png"/></p>
                        <p v-else-if="index==1"><img  src="../../images/2.png"/></p>
                        <p v-else-if="index==2"><img src="../../images/3.png"/></p>
                        <p v-else>{{index+1}}</p>
                    </Col>
                    <Col span="5">
                        <img src="../../images/nodata1.jpg"/>
                    </Col>
                    <Col span="5">{{item.class}}</Col>
                    <Col span="5">{{item.userName}}</Col>
                    <Col span="5">+{{item.testScore}}</Col>
                </Row>
            </div>
        </Col>
        <Modal
            v-model="flag"
            title="活动风采"
            @on-ok="ok"
            @on-cancel="cancel"
            :width="550"
            :height="500">
            <div class="demo-upload-list" v-if="actImgs.length>0" v-for="item in actImgs">
                <template>
                    <img :src="item.src">
                    <div class="demo-upload-list-cover">
                        <Icon type="ios-trash-outline" @click.native="handleRemove(item)"></Icon>
                    </div>
                </template>
            </div>
            <Upload
                ref="upload"
                :paste="true"
                :multiple="false"
                :default-file-list="uploadList"
                :format="['jpg','jpeg','png']"
                :max-size="2048"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                :before-upload="handleBeforeUpload"
                type="drag"
                action=""
                style="display: inline-block;width:58px;">
                <div style="width: 58px;height:58px;line-height: 58px;">
                    <Icon type="ios-camera" size="20"></Icon>
                </div>
            </Upload>
        </Modal>
    </div>
</template>
<script>
export default {
    name:'actDetail',
    data(){
        return{
            flag:false,
            partUsers:[
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
                {
                    userNumber:'2225554484',
                    userName:'yelanlan',
                    class:"计173-1",
                    testScore:10,
                    headAvatar:'../../images/nodata1.jpg'
                },
            ],
            actInfo:{
                id:'',
                actName:'',
                actHost:'',
                actDesc:'',
            },
            actImgs:[
                {src:require("../../images/1.png")},
                {src:require("../../images/11.png")},
                {src:require("../../images/22.png")}
            ],
            uploadImg:''
        }
    },
    watch:{
        $route:{
            immediate: true, // 一旦监听到路由的变化立即执行
            handler(newval,oldval){
                if(newval.path == '/act/actDetail')
                    if(this.$route.query !== null && this.$route.query !== undefined && JSON.stringify(this.$route.query)!=='{}'){
                        this.actInfo = JSON.parse(this.$route.query.actInfo);
                    }
            }   
        }
    },
    mounted(){
    },
    methods:{
        ok(){
           this.$Message.success("ok")
        },
        cancel(){
            alert
            this.flag = false;
        },
        compeleteImg(){
            this.flag = true;
        },
        handleRemove (file) {
            this.uploadList.splice(fileList.indexOf(file), 1);
        },
        handleFormatError (file) {
            this.$Notice.warning({
                title: '图片格式不正确',
                desc: '图片  '  + file.name + ' 格式不正确, 请上传的图片格式为 jpg / png.'
            });
        },
        handleMaxSize (file) {
            this.$Notice.warning({
                title: '图片太大了',
                desc: '图片  ' + file.name + ' 太大了，已经超过了2M.'
            });
        },
        handleBeforeUpload (file) {
            const check = this.actImgs.length < 5;
            if (!check) {
                this.$Notice.warning({title: '最多可上传 5 张图片'});
            }
            let param ={
                id:this.actInfo.id,
                imgFile:file,
            }
            console.log(file)
            this.actImgs.push(file)
            return check;
        }
    }
}
</script>
<style lang="less" scoped>
.actDetail{
    margin: 5px;
    .actInfo{
        padding: 10px;
        width:98%;
        height: 320px;
        background: #fff;
        overflow: hidden;
        text-align: left;
        h2{
            margin:0 10px 8px;
        }
        p{
            width: 100%;
            margin:0 10px 8px;
            white-space:nowrap;/*只对中文起作用，强制换行*/
            overflow: hidden; 
            text-overflow:ellipsis;
        }
        img{
            height: 15px;
            width: 15px;
        }
        .btns{
            margin: 10px 0px 0px 10px;
        }
    }
    .imgLunBo{
        padding: 10px;
        width: 100%;
        background: #fff;
        height: 320px;
        text-align: center;
        img{
            height: 280px;
            width: 300px;
        }
    }
    .actDesc{
        padding: 10px;
        width: 100%;
        height: 236px;
        background: #fff;
        margin-top: 10px;
        overflow-y: auto;
        text-align: left;
        p{
            text-indent: 2em;
        }
    }
}
    .demo-upload-list{
        display: inline-block;
        width: 60px;
        height: 60px;
        text-align: center;
        line-height: 60px;
        border: 1px solid transparent;
        border-radius: 4px;
        overflow: hidden;
        background: #fff;
        position: relative;
        box-shadow: 0 1px 1px rgba(0,0,0,.2);
        margin-right: 4px;
    }
    .demo-upload-list img{
        width: 100%;
        height: 100%;
    }
    .demo-upload-list-cover{
        display: none;
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background: rgba(0,0,0,.6);
    }
    .demo-upload-list:hover .demo-upload-list-cover{
        display: block;
    }
    .demo-upload-list-cover i{
        color: #fff;
        font-size: 20px;
        cursor: pointer;
        margin: 0 2px;
    }
.userRank{
    width: 100%;
    height: 566px;
    margin: 5px 10px;
    background: #fff;
    overflow-y: auto;
    img{
        border-radius: 50%;
        height: 25px;
        width: 25px;
    }
    .rankRow{
        padding: 10px 5px 5px ;
        text-align: center; 
        background: rgb(92, 167, 218);
    }
    .firstRank{
        background: rgb(229, 133, 192);
    }
    .secondRank{
        background: rgb(231, 163, 99);
    }
    .thirdRank{
        background: rgb(230, 215, 84);
    }
    .othorRank{
        background: #fff;
    }
}
</style>