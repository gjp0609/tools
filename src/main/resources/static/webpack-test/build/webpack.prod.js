const common = require('./webpack.base.js');
const merge = require('webpack-merge');
const path = require('path');

module.exports = merge(common, {
    module: {},
    plugins: [],
    mode: 'production',
    output: {
        filename: 'js/[name].[contenthash].js', // contenthash 若文件内容无变化，则 contenthash 名称不变
        path: path.resolve(__dirname, '../dist')
    }
});
