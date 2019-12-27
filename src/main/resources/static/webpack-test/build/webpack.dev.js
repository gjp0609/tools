const common = require('./webpack.base.js');
const merge = require('webpack-merge');
const path = require('path');

module.exports = merge(common, {
    devtool: 'eval',
    devServer: {
        contentBase: '../dist',
        compress: true,
        clientLogLevel: 'none',
        host: '127.0.0.1',
        port: 8089,
        proxy: {
            '/': {
                target: 'http://127.0.0.1:80',
                changeOrigin: true,
                pathRewrite: {
                    '^/': '/'
                }
            }
        }
    },
    output: {
        filename: 'js/[name].[hash].js', // 每次保存 hash 都变化
        path: path.resolve(__dirname, '../dist')
    },
    module: {},
    mode: 'development'
});
