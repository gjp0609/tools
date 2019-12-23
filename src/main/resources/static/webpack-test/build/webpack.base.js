const webpack = require('webpack');
const path = require('path');
const VueLoaderPlugin = require('vue-loader/lib/plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HappyPack = require('happypack');
const os = require('os');
const happyThreadPool = HappyPack.ThreadPool({ size: os.cpus().length });
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');
const TerserPlugin = require('terser-webpack-plugin');

module.exports = {
    entry: './src/index.js', // 入口
    module: {
        rules: [
            {
                test: /\.vue$/,
                loader: 'vue-loader'
            },
            {
                test: /\.(sa|sc|c)ss$/,
                use: [
                    {
                        loader: MiniCssExtractPlugin.loader,
                        options: {
                            publicPath: '../'
                        }
                    },
                    'css-loader',
                    'postcss-loader',
                    'sass-loader'
                ]
            },
            {
                test: /\.(eot|ttf|woff|woff2)$/,
                loader: 'file-loader',
                options: {
                    name: 'fonts/[hash].[ext]'
                }
            },
            {
                test: /\.js$/,
                // 把对.js 的文件处理交给id为happyBabel 的HappyPack 的实例执行
                loader: 'happypack/loader?id=happyBabel',
                // 排除node_modules 目录下的文件
                exclude: /node_modules/
            },
            {
                test: /\.(png|svg|jpg|gif)$/,
                use: [
                    {
                        loader: 'file-loader',
                        options: {
                            limit: 5000,
                            name: 'imgs/[hash].[ext]'
                        }
                    },
                    {
                        loader: 'image-webpack-loader',
                        options: {
                            bypassOnDebug: true,
                            mozjpeg: {
                                progressive: true,
                                quality: 65
                            },
                            optipng: {
                                enabled: false
                            },
                            pngquant: {
                                quality: '65-90',
                                speed: 4
                            },
                            gifsicle: {
                                interlaced: false
                            }
                        }
                    }
                ]
            }
        ]
    },
    plugins: [
        new webpack.HashedModuleIdsPlugin(),
        new VueLoaderPlugin(),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, '../index.html')
        }),
        require('autoprefixer'),
        new CleanWebpackPlugin({}),
        new MiniCssExtractPlugin({
            filename: 'css/[name].[hash].css',
            chunkFilename: 'css/[id].[hash].css'
        }),
        new HappyPack({
            // 用 id 来标识 HappyPack 处理类文件
            id: 'happyBabel',
            // 如何处理, 用法和 loader 的配置一样
            loaders: [
                {
                    loader: 'babel-loader?cacheDirectory=true'
                }
            ],
            // 共享进程池
            threadPool: happyThreadPool,
            // 允许 HappyPack 输出日志
            verbose: true
        })
    ],
    optimization: {
        // 分离chunks
        splitChunks: {
            chunks: 'all',
            cacheGroups: {
                vendor: {
                    name: 'vendor',
                    test: /[\\/]node_modules[\\/]/,
                    priority: 10,
                    chunks: 'initial' // 只打包初始时依赖的第三方
                }
            }
        },
        minimize: true,
        minimizer: [
            // 压缩JS
            new TerserPlugin(),
            // 压缩css
            new OptimizeCSSAssetsPlugin({})
        ]
    },
    externals: {
        // 如 vendor.js 过大则可通过独立引入的方式减小 js 体积
        // 'vue': 'Vue',
        // 'element-ui': 'Element',
        // 'axios': 'axios'
    }
};
