<template>
    <div class="index">
        <span>{{ test }}</span>
        <el-button @click="asd">qwe</el-button>
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        name: 'App',
        data() {
            return {
                test: 1
            };
        },
        methods: {
            asd() {
                alert(123111);
                axios
                    .get('http://httpbin.org/get?param=test')
                    .then((resp) => {
                        console.log(resp);
                    })
                    .catch((e) => {
                        console.log(e);
                    });
                axios
                    .get('/test/get?s=123')
                    .then((resp) => {
                        console.log(resp);
                    })
                    .catch((e) => {
                        console.log(e);
                    });
            }
        }
    };
</script>

<style lang="scss" scoped>
    .index {
        span {
            color: #00c4a7;
        }
    }
</style>
