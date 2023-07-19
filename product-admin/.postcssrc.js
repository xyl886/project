// https://github.com/michael-ciniawsky/postcss-load-config

const {resolve} = require("path");
module.exports = {
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  "plugins": {
    "postcss-import": {},
    "postcss-url": {},
    // to edit target browsers: use "browserslist" field in package.json
    "autoprefixer": {}
  }
}
