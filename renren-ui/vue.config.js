/**
 * 配置参考: https://cli.vuejs.org/zh/config/
 */

const path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir);
}
module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
  chainWebpack: config => {
    // const svgRule = config.module.rule('svg')
    // svgRule.uses.clear()
    // svgRule
    //   .test(/\.svg$/)
    //   .use('svg-sprite-loader')
    //   .loader('svg-sprite-loader')
    // config.module.rule('svg').exclude.add(resolve('src/icons')).end();
    // 新加一个icons的处理规则,用svg-sprite-loader处理，将上面那个目录添加到这个规则的处理范围内
    // config.module
    //     .rule('svg-sprite-loader')  // 新添加的处理规则
    //     .test(/\.svg$/)
    //     .include                   // 添加
    //     .add(resolve('src/icons')) //处理svg目录
    //     .end()
    //     .use('svg-sprite-loader') // 使用svg-sprite-loader处理
    //     .loader('svg-sprite-loader')
    //     .options({
    //       symbolId: 'icon-[name]'
    //     })
    config.resolve.alias.set("@", resolve("src"));
  },
  productionSourceMap: false,
  devServer: {
    open: true,
    port: 8001,
    overlay: {
      errors: true,
      warnings: true
    }
  }
}
