import './iconfont'

const svgFiles = require.context('./svg', true, /\.svg$/)
svgFiles.keys().map(item => svgFiles(item))
