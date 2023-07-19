<template>
  <header ref="header">
    <div v-for="image in images" :key="image">
      <img :src="image"  alt=""/>
    </div>
  </header>
</template>

<script>
export default {
  name: 'Header',
  data () {
    return {
      startX: 0,
      images: [
        require('../../../public/img/userheader1.png'),
        require('../../../public/img/userheader2.png'),
        require('../../../public/img/userheader3.png'),
        require('../../../public/img/userheader4.png'),
        require('../../../public/img/userheader5.png'),
        require('../../../public/img/userheader6.png')
      ]
    }
  },
  mounted () {
    this.setupHeader()
  },
  methods: {
    setupHeader () {
      const header = this.$refs.header
      const images = header.querySelectorAll('div>img')

      header.addEventListener('mousemove', (e) => {
        let offsetX = e.clientX - this.startX + 482
        let percentage = offsetX / window.outerWidth
        let offset = 15 * percentage
        let blur = 20
        for (let [index, image] of images.entries()) {
          offset *= 1.3
          let blurValue =
            (Math.pow(index / images.length - percentage, 2) * blur).toFixed(
              2
            ) + 'px'
          image.style.setProperty('--offset', `${offset}px`)
          image.style.setProperty('--blur', blurValue)
        }
      })

      header.addEventListener('mouseover', (e) => {
        this.startX = e.clientX
        for (let image of images.entries()) {
          image.style.transition = 'none'
        }
      })

      header.addEventListener('mouseout', () => {
        let offsetX = 482
        let blur = 20
        let percentage = offsetX / window.outerWidth
        let offset = 15 * percentage
        for (let [index, image] of images.entries()) {
          offset *= 1.3
          let blurValue =
            (Math.pow(index / images.length - percentage, 2) * blur).toFixed(
              2
            ) + 'px'
          image.style.setProperty('--offset', `${offset}px`)
          image.style.setProperty('--blur', blurValue)
          image.style.transition = 'all .3s ease'
        }
      })

      window.addEventListener('load', () => {
        let offsetX = 482
        let blur = 20
        let percentage = offsetX / window.outerWidth
        let offset = 15 * percentage
        for (let [index, image] of images.entries()) {
          offset *= 1.3
          let blurValue =
            (Math.pow(index / images.length - percentage, 2) * blur).toFixed(
              2
            ) + 'px'
          image.style.setProperty('--offset', `${offset}px`)
          image.style.setProperty('--blur', blurValue)
        }
      })
    }
  }
}
</script>

<style scoped>
header {
  height: 155px;
  position: relative;
  overflow: hidden;
}

header>div {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  --offset: 0px;
  --blur: 2px;
}

header>div>img {
  display: block;
  width: 110%;
  height: 100%;
  object-fit: cover;
  transform: translateX(var(--offset));
  filter: blur(var(--blur));
}
</style>
