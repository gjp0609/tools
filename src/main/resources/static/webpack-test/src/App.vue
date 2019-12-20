<template>
    <div class="index">
        <span>{{test}}</span>
        <el-button @click="asd">qwe</el-button>
    </div>
</template>

<script>
    export default {
        name: "App",
        data() {
            return {
                test: 1
            }
        },
        methods: {
            asd() {
                alert(123)
            }
        }
    }
</script>

<style lang="scss" scoped>
    .index {
        span {
            color: #00c4a7;
        }
    }
</style>