const common = require('./webpack.base.js');
const merge = require('webpack-merge');
const path = require('path');

module.exports = merge(common, {
    devtool: 'inline-source-map',
    devServer: {
        contentBase: '../dist',
        port: 8089
    },
    output: {
        filename: 'js/[name].[hash].js', // 每次保存 hash 都变化
        path: path.resolve(__dirname, '../dist')
    },
    module: {},
    mode: 'development'
});
