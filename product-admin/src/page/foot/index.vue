<template>
<!--  <div style="font-size: 14px;">-->
<!--    <div style="height: 40px;text-align: center;line-height: 40px;">-->
<!--      Powered by-->
<!--      <span style="color: #409EFF;cursor: pointer;" @click="homePage()">@我的网站</span>-->
<!--    </div>-->
<!--  </div>-->
  <footer>
    <div class="container">
      <div class="row">
        <div class="col-md-4">
          <h3>关于我们</h3>
          <p>校园墙是一个社交平台，旨在为学生提供一个分享和交流的空间。</p>
        </div>
        <div class="col-md-4" style="margin-top: 10px">
          <h3>联系我们</h3>
          <p>电子邮件：contact@campuswall.com</p>
          <p>电话：+86 1234567890</p>
        </div>
<!--        <div class="col-md-4">-->
<!--          <h3>关注我们</h3>-->
<!--          <ul class="social-icons">-->
<!--            <li><a href="#"><i class="fa fa-facebook"></i></a></li>-->
<!--            <li><a href="#"><i class="fa fa-twitter"></i></a></li>-->
<!--            <li><a href="#"><i class="fa fa-linkedin"></i></a></li>-->
<!--            <li><a href="#"><i class="fa fa-instagram"></i></a></li>-->
<!--          </ul>-->
<!--        </div>-->
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  data () {
    return {
      path: ''
    }
  },
  mounted () {
    this.path = this.$router.currentRoute.path
  },
  methods: {
    homePage () {
      this.$router.push({path: '/index'})
    }
  }
}
</script>
<style scoped>
footer {
  color: #666;
  padding: 10px 0;
  text-align: center;
}

footer h3 {
  color: #555;
  margin-top: 0;
}

footer p {
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 0;
}

.social-icons {
  list-style: none;
  padding: 0;
  margin: 20px 0 0;
}

.social-icons li {
  display: inline-block;
  margin-right: 10px;
}

.social-icons li:last-child {
  margin-right: 0;
}

.social-icons a {
  display: block;
  width: 40px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  color: #fff;
  background-color: #333;
  border-radius: 50%;
}

.fa {
  font-size: 20px;
}

@media (max-width: 767px) {
  footer {
    padding: 40px 0;
  }

  .social-icons {
    margin: 10px 0 0;
    text-align: center;
  }

  .social-icons li {
    display: inline-block;
    margin: 0 5px;
  }
}
</style>
