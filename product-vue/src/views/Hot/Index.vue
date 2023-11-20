<template>
  <div class="hotSearchMain">
    <div class="box">
<!--      <div class="nav">-->
<!--        <div class="siteBox">-->
<!--          <svg-icon :icon-class="svgList[selectSite]"></svg-icon>-->
<!--          <i @click="dialogVisible = true" class="el-icon-caret-bottom"></i>-->
<!--        </div>-->
<!--        <el-input class="input" v-model="text" placeholder="请输入内容" @keyup.enter.native="handleCommit"></el-input>-->
<!--        <div class="searchCommit" @click="handleCommit">-->
<!--          <i class="el-icon-search"></i>-->
<!--        </div>-->
<!--      </div>-->
      <div class="hot">
        <div class="weibo">
          <div class="title">
            <span>微博热搜</span>
            <svg data-v-84e2c972="">
              <defs data-v-84e2c972="">
                <pattern data-v-84e2c972="" id="water" width=".25" height="1.1"
                         patternContentUnits="objectBoundingBox">
                  <path data-v-84e2c972="" fill="#fff"
                        d="M0.25,1H0c0,0,0-0.659,0-0.916c0.083-0.303,0.158,0.334,0.25,0C0.25,0.327,0.25,1,0.25,1z">
                  </path>
                </pattern>
                <g data-v-84e2c972="" id="eff">
                  <rect data-v-84e2c972="" fill="url(#water)" x="-119.461" y="0" width="1200" height="120"
                        opacity="0.3">
                    <animate data-v-84e2c972="" attributeType="xml" attributeName="x" from="-300" to="0"
                             repeatCount="indefinite" dur="10s"></animate>
                  </rect>
                  <rect data-v-84e2c972="" fill="url(#water)" y="5" width="1600" height="125"
                        opacity="0.3" x="-399.447">
                    <animate data-v-84e2c972="" attributeType="xml" attributeName="x" from="-400" to="0"
                             repeatCount="indefinite" dur="13s"></animate>
                  </rect>
                </g>
              </defs>
              <use data-v-84e2c972="" xlink:href="#eff" opacity="1" style="mix-blend-mode: normal;"></use>
            </svg>
          </div>
          <ul style="padding: 0">
            <li style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" v-for="(item, index) in weiboList" :key="index" @click="go(item.url)">
              <span style="text-align: left;margin-right: 10px" :style="getIconClass(index)">
                <span v-if="index < 3">{{ index + 1 }}</span>
                <span v-else>{{ index + 1 }}</span>
              </span>
              <el-tooltip :content="item.summary" placement="right">
                <span>{{ item.keyword }}</span>
              </el-tooltip>
            </li>
          </ul>
        </div>
        <div class="baidu">
          <div class="title">
            <span>百度热搜</span>
            <svg data-v-84e2c972="">
              <defs data-v-84e2c972="">
                <pattern data-v-84e2c972="" id="water" width=".25" height="1.1"
                         patternContentUnits="objectBoundingBox">
                  <path data-v-84e2c972="" fill="#fff"
                        d="M0.25,1H0c0,0,0-0.659,0-0.916c0.083-0.303,0.158,0.334,0.25,0C0.25,0.327,0.25,1,0.25,1z">
                  </path>
                </pattern>
                <g data-v-84e2c972="" id="eff">
                  <rect data-v-84e2c972="" fill="url(#water)" x="-119.461" y="0" width="1200" height="120"
                        opacity="0.3">
                    <animate data-v-84e2c972="" attributeType="xml" attributeName="x" from="-300" to="0"
                             repeatCount="indefinite" dur="10s"></animate>
                  </rect>
                  <rect data-v-84e2c972="" fill="url(#water)" y="5" width="1600" height="125"
                        opacity="0.3" x="-399.447">
                    <animate data-v-84e2c972="" attributeType="xml" attributeName="x" from="-400" to="0"
                             repeatCount="indefinite" dur="13s"></animate>
                  </rect>
                </g>
              </defs>
              <use data-v-84e2c972="" xlink:href="#eff" opacity="1" style="mix-blend-mode: normal;"></use>
            </svg>
          </div>
          <ul style="padding: 0">
            <li  style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" v-for="(item, index) in baiduList" :key="index" @click="go(item.url)">
              <span style="text-align: left;margin-right: 10px" :style="getIconClass(index)">
                <span v-if="index < 3">{{ index + 1 }}</span>
                <span v-else>{{ index + 1 }}</span>
              </span>
              <el-tooltip :content="item.summary" placement="right">
                <span>{{ item.keyword }}</span>
              </el-tooltip>
            </li>
          </ul>
        </div>

        <div class="zhihu">
          <div class="title">
            <span>知乎热搜</span>
            <svg data-v-84e2c972="">
              <defs data-v-84e2c972="">
                <pattern data-v-84e2c972="" id="water" width=".25" height="1.1"
                         patternContentUnits="objectBoundingBox">
                  <path data-v-84e2c972="" fill="#fff"
                        d="M0.25,1H0c0,0,0-0.659,0-0.916c0.083-0.303,0.158,0.334,0.25,0C0.25,0.327,0.25,1,0.25,1z">
                  </path>
                </pattern>
                <g data-v-84e2c972="" id="eff">
                  <rect data-v-84e2c972="" fill="url(#water)" x="-119.461" y="0" width="1200" height="120"
                        opacity="0.3">
                    <animate data-v-84e2c972="" attributeType="xml" attributeName="x" from="-300" to="0"
                             repeatCount="indefinite" dur="10s"></animate>
                  </rect>
                  <rect data-v-84e2c972="" fill="url(#water)" y="5" width="1600" height="125"
                        opacity="0.3" x="-399.447">
                    <animate data-v-84e2c972="" attributeType="xml" attributeName="x" from="-400" to="0"
                             repeatCount="indefinite" dur="13s"></animate>
                  </rect>
                </g>
              </defs>
              <use data-v-84e2c972="" xlink:href="#eff" opacity="1" style="mix-blend-mode: normal;"></use>
            </svg>
          </div>
          <ul style="padding: 0">
            <li  style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" v-for="(item, index) in zhihuList" :key="index" @click="go(item.url)">
              <span style="text-align: left;margin-right: 10px" :style="getIconClass(index)">
                <span v-if="index < 3">{{ index + 1 }}</span>
                <span v-else>{{ index + 1 }}</span>
              </span>
              <el-tooltip :content="item.summary" placement="right">
                <span>{{ item.keyword }}</span>
              </el-tooltip>
            </li>
          </ul>
        </div>
        <div class="csdn">
          <div class="title">
            <span>CSDN热搜</span>
            <svg data-v-84e2c972="">
              <defs data-v-84e2c972="">
                <pattern data-v-84e2c972="" id="water" width=".25" height="1.1"
                         patternContentUnits="objectBoundingBox">
                  <path data-v-84e2c972="" fill="#fff"
                        d="M0.25,1H0c0,0,0-0.659,0-0.916c0.083-0.303,0.158,0.334,0.25,0C0.25,0.327,0.25,1,0.25,1z">
                  </path>
                </pattern>
                <g data-v-84e2c972="" id="eff">
                  <rect data-v-84e2c972="" fill="url(#water)" x="-119.461" y="0" width="1200" height="120"
                        opacity="0.3">
                    <animate data-v-84e2c972="" attributeType="xml" attributeName="x" from="-300" to="0"
                             repeatCount="indefinite" dur="10s"></animate>
                  </rect>
                  <rect data-v-84e2c972="" fill="url(#water)" y="5" width="1600" height="125"
                        opacity="0.3" x="-399.447">
                    <animate data-v-84e2c972="" attributeType="xml" attributeName="x" from="-400" to="0"
                             repeatCount="indefinite" dur="13s"></animate>
                  </rect>
                </g>
              </defs>
              <use data-v-84e2c972="" xlink:href="#eff" opacity="1" style="mix-blend-mode: normal;"></use>
            </svg>
          </div>
          <ul style="padding: 0">
            <li  style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" v-for="(item, index) in csdnList" :key="index" @click="go(item.url)">
              <span style="text-align: left;margin-right: 10px" :style="getIconClass(index)">
                <span v-if="index < 3">{{ index + 1 }}</span>
                <span v-else>{{ index + 1 }}</span>
              </span>
              <el-tooltip :content="item.summary" placement="right">
               <span>{{ item.keyword }}</span>
              </el-tooltip>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <el-dialog :lock-scroll="false" title="切换搜索引擎" :visible.sync="dialogVisible" width="30%">
      <el-radio-group v-model="tabPosition" style="margin-bottom: 30px;">
        <el-radio-button v-for="(item, index) in siteList" :key="index" :label="index">{{ item }}</el-radio-button>

      </el-radio-group>
      <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleClose">确 定</el-button>
            </span>
    </el-dialog>

  </div>
</template>
<script>
import { getHot } from '../../api/hot'
export default {
  data () {
    return {
      baiduList: [],
      weiboList: [],
      zhihuList: [],
      csdnList: [],
      text: null,
      loading: [],
      siteList: ['百度', 'Google', '知乎', 'Csdn', '码云', 'Github', '哔哩哔哩'],
      dialogVisible: false,
      tabPosition: 0,
      selectSite: 0,
      svgList: ['baidu', 'google', 'zhihu', 'csdn', 'gitee', 'github', 'bili'],
      searchUrl: [
        'https://www.baidu.com/s?&wd=',
        'https://www.google.com/search?q=',
        'https://www.zhihu.com/search?type=content&q=',
        'https://so.csdn.net/so/search?spm=1000.2115.3001.4498&q=',
        'https://search.gitee.com/?skin=rec&type=repository&q=',
        'https://github.com/search?q=',
        'https://search.bilibili.com/all?keyword=']
    }
  },

  created () {
    this.openLoading()
    getHot('baidu').then(res => {
      this.baiduList = res.data
    })
    getHot('weibo').then(res => {
      this.weiboList = res.data
    })
    getHot('zhihu').then(res => {
      this.zhihuList = res.data
    })
    getHot('csdn').then(res => {
      this.csdnList = res.data
    })
    this.loading.close()
  },
  methods: {
    color (index) {
      if (index === 0) {
        return {color: 'red'}
      }
      if (index === 1) {
        return {color: '#ea9518'}
      }
      if (index === 2) {
        return {color: '#1296db'}
      }
    },
    getIconClass (index) {
      if (index === 0) {
        return {color: '#fb5835', fontSize: '18px', fontWeight: 'bold'}
      } else if (index === 1) {
        return {color: '#fe6434', fontSize: '18px', fontWeight: 'bold'}
      } else if (index === 2) {
        return {color: '#ff6a00', fontSize: '18px', fontWeight: 'bold'}
      } else {
        return {color: '#d1d1d1', fontSize: '18px', fontWeight: 'bold'}
      }
    },
    go (url) {
      window.open(url, '_blank')
    },
    handleCommit () {
      if (!this.text) {
        this.$message.error('请输入搜索内容')
        return
      }
      window.open(this.searchUrl[this.selectSite] + this.text, '_blank')
    },
    handleClose () {
      this.dialogVisible = false
      this.selectSite = this.tabPosition
      this.$message.success('切换成功')
    },
    // 打开加载层
    openLoading: function () {
      this.loading = this.$loading({
        lock: true,
        text: '正在加载中~',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
        fullscreen: false
      })
    }
  }
}
</script>
<style scoped>
li:hover{
  background-color: #DCDFE6;
}
</style>
<style lang="scss" scoped>
@media screen and (max-width: 1118px) {
  .hotSearchMain {
    display: flex;
    justify-content: center;
    position: relative;

    .box {
      width: 100%;
      height: 100%;
      padding: 10px;

      .nav {
        margin: 0 auto;
        display: block;
        width: 100%;
        height: 40px;

        /deep/ .el-input__inner {
          border-radius: 0 !important;
        }

        .siteBox {
          display: inline-block;
          height: calc(100% - 2px);
          line-height: 36px;
          border: 1px solid #DCDFE6;
          border-right: none;
          padding-left: 20px;
          padding-right: 20px;

          svg {
            width: 25px;
            height: 25px;
            vertical-align: -8px;
          }

          i {
            margin-left: 10px;
            cursor: pointer;
            font-size: 14px;
          }

        }

        .input {
          display: inline-block;
          width: 60%;
          border-radius: 0 !important;
        }

        .searchCommit {
          display: inline-block;
          height: calc(100% - 2px);
          line-height: 36px;
          border: 1px solid #DCDFE6;
          border-left: none;
          padding-left: 20px;
          padding-right: 20px;
          cursor: pointer;
        }
      }

      .hot {
        margin-top: 50px;

        .weibo .title {
          background-color: #d52c2b;
          background-image: radial-gradient(circle at 50% 3%, #d63736, #d52c2b);
        }

        .baidu .title {
          background-color: #4e6ef2;
          background-image: radial-gradient(circle at 50% -45%, #53a7f5, #4e6ef2);
        }

        .zhihu .title {
          background-color: #53a7f5;
          background-image: radial-gradient(circle at 50% -45%, #53a7f5, #4e6ef2);
        }

        .csdn .title {
          background-color: #fc5632;
          background-image: radial-gradient(circle at 50% 3%, #f07a56, #fc5632);
        }

        .weibo,
        .baidu,
        .zhihu,
        .csdn {
          width: 47%;
          display: inline-block;
          margin-right: 10px;

          .title {
            width: 100%;
            height: 78px;
            text-align: center;
            position: relative;
            overflow: hidden;

            svg {
              margin-top: 20px;
              width: 100%;
            }

            span {
              color: #fff;
              font-size: 16px;
              font-weight: bold;
              position: absolute;
              top: 40%;
              left: 0;
              right: 0;
              bottom: 0;
            }
          }

          ul {
            list-style: none;
            margin-top: 10px;

            li {
              margin-bottom: 20px;
              cursor: pointer;
              font-size: 13px;
              height: 100%;
              padding: 5px;

              &:hover {
                background-color: #DCDFE6;
              }

              span {
                color: var(--text-color);
              }

              .index {
                display: inline-block;
                text-align: center;
                width: 25px;
                height: 25px;
                position: relative;

                svg {
                  width: 30px;
                  height: 30px;
                  position: absolute;
                  top: -4px;
                  left: -3px;
                  fill: currentColor;
                  color: #bfbfbf;
                }

              }
            }
          }
        }
      }

    }
  }
}

@media screen and (min-width: 1119px) {
  .hotSearchMain {
    display: flex;
    justify-content: center;
    position: relative;

    .box {
      width: 70%;
      height: 100%;
      padding: 10px;

      .nav {
        margin: 0 auto;
        display: block;
        width: 70%;
        height: 40px;

        /deep/ .el-input__inner {
          border-radius: 0 !important;
          background-color: var(--background-color);

        }

        .siteBox {
          display: inline-block;
          height: calc(100% - 2px);
          border: 1px solid #DCDFE6;
          line-height: 36px;
          border-right: none;
          padding-left: 20px;
          padding-right: 20px;

          svg {
            width: 25px;
            height: 25px;
            vertical-align: -8px;
          }

          i {
            margin-left: 10px;
            cursor: pointer;
            font-size: 14px;
          }

        }

        .input {
          display: inline-block;
          width: 60%;
          border-radius: 0 !important;
        }

        .searchCommit {
          display: inline-block;
          height: calc(100% - 2px);
          line-height: 36px;
          border: 1px solid #DCDFE6;
          border-left: none;
          padding-left: 20px;
          padding-right: 20px;
          cursor: pointer;
        }
      }

      .hot {
        margin-top: 50px;
        display: flex;

        .weibo .title {
          background-color: #d52c2b;
          background-image: radial-gradient(circle at 50% 3%, #d63736, #d52c2b);
        }

        .baidu .title {
          background-color: #4e6ef2;
          background-image: radial-gradient(circle at 50% -45%, #53a7f5, #4e6ef2);
        }

        .zhihu .title {
          background-color: #53a7f5;
          background-image: radial-gradient(circle at 50% -45%, #53a7f5, #4e6ef2);
        }

        .csdn .title {
          background-color: #fc5632;
          background-image: radial-gradient(circle at 50% 3%, #f07a56, #fc5632);
        }

        .weibo,
        .baidu,
        .zhihu,
        .csdn {
          width: 250px;
          display: inline-block;
          margin-right: 30px;

          .title {
            width: 100%;
            height: 78px;
            text-align: center;
            position: relative;
            overflow: hidden;

            svg {
              margin-top: 20px;
            }

            span {
              color: #fff;
              font-size: 16px;
              font-weight: bold;
              position: absolute;
              top: 40%;
              left: 0;
              right: 0;
              bottom: 0;
            }
          }

          ul {
            list-style: none;
            margin-top: 10px;
            border-right: 1px solid var(--border-line);

            li {
              margin-bottom: 20px;
              cursor: pointer;
              font-size: 13px;
              height: 100%;
              padding: 5px;

              &:hover {
                background-color: #DCDFE6;
              }

              span {
                color: var(--text-color);
              }

              .index {
                display: inline-block;
                text-align: center;
                width: 25px;
                height: 25px;
                position: relative;
                margin-right: 10px;

                svg {
                  width: 30px;
                  height: 30px;
                  position: absolute;
                  top: -4px;
                  left: -3px;
                  fill: currentColor;
                  color: #bfbfbf;
                }

              }
            }
          }
        }
      }

    }
  }
}
</style>
