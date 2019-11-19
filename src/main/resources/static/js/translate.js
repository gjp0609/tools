// ==UserScript==
// @name         * 搜狗/百度翻译
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       OnySakura
// @include      *
// @grant        GM_xmlhttpRequest
// ==/UserScript==

(function () {
    if (window.top != window.self) {
        // iframe直接返回
        return;
    }
    // 选中的文本
    let translateText = '';
    const SOGO_CONFIG = {
        enable: true,
        status: false,
        COLOR: '#fd6853',
        CODE: 'sougo',
        URL: 'https://fanyi.sogou.com/reventondc/api/sogouTranslate',
        PID: '92cc84c2b40df0a7def169866840c048',
        KEY: 'b4a8fba0d1b872eb87a81cc6b833a4b9',
        initParam: function () {
            translateText = translateText.trim().replace(/\n/g, ',');
            let salt = getSalt();
            let src = SOGO_CONFIG.PID + translateText + salt + SOGO_CONFIG.KEY;
            let sign = MD5(src);
            let encodedTranslateText = encodeURI(translateText);
            return "q=" + encodedTranslateText + "&pid=" + SOGO_CONFIG.PID + "&to=zh-CHS&from=auto&salt=" + salt + "&sign=" + sign;
        },
        parseResult(json) {
            if (json.query) {
                return json.translation;
            } else {
                return JSON.stringify(json);
            }
        }
    };
    const BAIDU_CONFIG = {
        enable: true,
        status: false,
        COLOR: '#398bfb',
        CODE: 'baidu',
        URL: 'https://fanyi-api.baidu.com/api/trans/vip/translate',
        PID: '20180606000172703',
        KEY: '8Vt698YbGe9qWugpNZml',
        initParam: function () {
            translateText = translateText.trim().replace(/\n/g, ',');
            let salt = getSalt();
            let src = BAIDU_CONFIG.PID + translateText + salt + BAIDU_CONFIG.KEY;
            let sign = MD5(src);
            let encodedTranslateText = encodeURI(translateText);
            return `q=${encodedTranslateText}&appid=${BAIDU_CONFIG.PID}&to=zh&from=auto&salt=${salt}&sign=${sign}`;
        },
        parseResult(json) {
            if (json.trans_result) {
                if (json.trans_result[0].dst) {
                    let dst = '' + json.trans_result[0].dst;
                    return decodeURI(dst);
                }
            }
            return JSON.stringify(json);
        }
    };
    const GOOGLE_CONFIG = {
        enable: true,
        status: false,
        COLOR: '#1fa463',
        CODE: 'google',
        URL: 'https://translate.google.cn/translate_a/single?client=gtx&dt=t&dt=bd&dj=1&source=input&hl=zh-CN&sl=auto&tl=zh-CN&',
        initParam: function () {
            translateText = translateText.trim().replace(/\n/g, ',');
            let encodedTranslateText = encodeURI(translateText);
            return "q=" + encodedTranslateText;
        },
        parseResult(json) {
            let result = '';
            console.log(json);
            if (json) {
                if (json.sentences) {
                    for (let item of json.sentences) {
                        result += item.trans;
                    }
                }
                if (json.dict) {
                    for (let item of json.dict) {
                        let terms = '';
                        for (let term of item.entry) {
                            terms += `
                                <span class="term">${term.word}
                                    <span class="tooltiptext">score: ${term.score}<br/>reverse: ${term.reverse_translation}</span>
                                </span>&nbsp;
                            `;
                        }
                        result += `
                            <div class="google_dict">
                                <span class="base_form">${item.base_form}</span>
                                    <span class="pos pos_${item.pos_enum}">: ${item.pos}</span>
                                    <span class="terms">${terms}
                                </span>
                            </div>
                        `;
                    }
                }
            }
            return result;
        }
    };
    let transList = [SOGO_CONFIG, BAIDU_CONFIG, GOOGLE_CONFIG];
    // 选择文本后展示的图标
    let showIcon = document.createElement("div");
    showIcon.id = "OnySakuraTranslateShowIcon";
    showIcon.innerHTML = "译";
    showIcon.style.display = 'none';
    document.body.appendChild(showIcon);
    // 样式
    let div_style = document.createElement("style");
    div_style.innerHTML = `
            #OnySakuraTranslateShowIcon {
                background-color: white;
                border: #fd6848 solid 2px;
                border-radius: 200px;
                color: #fd6848;
                box-sizing: border-box;
                width: 30px;
                height: 30px;
                text-align: center;
                line-height: 26px;
                cursor: pointer;
                position: fixed;
                z-index: 30000;
            }

            #OnySakuraTranslateShowIcon:hover {
                background-color: #fd6848;
                color: white;
                animation-duration: 1s;
            }

            #OnySakuraTranslateShowIcon:active {
                border-color: white;
            }

            #OnySakuraTranslateDiv {
                display: none;
                background-color: #FFFAF6;
                border: #fd6848 solid 2px;
                border-radius: 10px;
                padding: 5px;
                margin:auto;
                position: fixed;
                z-index: 100000001;
            }
            .google_dict {
                margin-left: 20px;
                margin-top: 10px;
            }
            
            .google_dict .base_form {
                font-size: 20px !important;
                font-family: "Sarasa Term SC", mononoki, monospace;
            }
            
            .google_dict .term {
                position: relative;
                display: inline-block;
                border-bottom: 1px dotted black;
            }
            
            .google_dict .term .tooltiptext {
                visibility: hidden;
                background-color: #fdc;
                color: #555;
                text-align: left;
                padding: 5px;
                border-radius: 5px;
                position: absolute;
                z-index: 1;
                white-space: pre;
                top: 200%;
                left: 0%;
            
            }
            
            .google_dict .term:hover .tooltiptext {
                visibility: visible;
            }
            
            /*.google_dict .term .tooltiptext::after {
                content: " ";
                position: absolute;
                bottom: 100%;
            
                left: 50%;
                margin-left: -5px;
                border-width: 5px;
                border-style: solid;
                border-color: transparent transparent #fdc transparent;
            }
            */
            .google_dict .pos {
                font-size: 10px;
                font-style: italic;
            }
            
            .google_dict .pos_1 {
                color: #369;
            }
            
            .google_dict .pos_2 {
                color: #396;
            }
            
            .google_dict .pos_3 {
                color: #639;
            }
            
            .google_dict .pos_4 {
                color: #693;
            }
            
            .google_dict .pos_5 {
                color: #936;
            }
            
            .google_dict .pos_6 {
                color: #963;
            }
        `;
    document.body.appendChild(div_style);
    // 翻译框
    let translateDiv = document.createElement("div");
    translateDiv.id = "OnySakuraTranslateDiv";
    document.body.appendChild(translateDiv);

    function selectText() {
        if (document.selection) {//For ie
            return document.selection.createRange().text;
        } else {
            return window.getSelection().toString();
        }
    }

    document.onmouseup = function (ev) {
        ev = ev || window.event;
        let left = ev.clientX, top = ev.clientY;
        setTimeout(function () {
            translateText = selectText();
            if (translateText.length > 0) {
                setTimeout(function () {
                    showIcon.style.display = 'block';
                    showIcon.style.left = left + 'px';
                    showIcon.style.top = top + 'px';
                }, 100);
            }
        }, 200);
    };

    function strToJson(str) {
        try {
            return (new Function("return " + str))();
        } catch (e) {
        }
        return str;
    }

    showIcon.onclick = function (ev) {
        ev = ev || window.event;
        let left = ev.clientX, top = ev.clientY;
        let height = document.body.clientHeight, width = document.body.clientWidth;
        translateDiv.style.display = "block";
        if (left > parseInt(width) / 2) {
            // 右
            translateDiv.style.right = width - left + 'px';
            translateDiv.style.left = '';
        } else {
            // 左
            translateDiv.style.left = left + 'px';
            translateDiv.style.right = '';
        }
        if (top > parseInt(height) / 2) {
            // 下
            translateDiv.style.bottom = height - top + 'px';
            translateDiv.style.top = '';
        } else {
            // 上
            translateDiv.style.top = top + 'px';
            translateDiv.style.bottom = '';
        }
        let innerHtml = `
                <div style="margin: 3px">
                    <span style="color: blueviolet">src: </span>
                    <span>${translateText}</span>
                </div>
            `;
        for (let item of transList) {
            if (item.enable) {
                innerHtml += `
                        <div style="margin: 3px">
                            <span style="color: ${item.COLOR}">${item.CODE}: </span>
                            <span>{{result${item.CODE}}}</span>
                        </div>
                    `;
            }
        }
        translateDiv.innerHTML = innerHtml;
        translate();
    };

    function translate() {
        let innerHTML = translateDiv.innerHTML;
        for (let item of transList) {
            if (item.enable) {
                GM_xmlhttpRequest({
                    method: "POST",
                    url: item.URL,
                    data: item.initParam(),
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded;",
                        "Accept": "application/json"
                    },
                    onload: function (xhr) {
                        let d = strToJson(xhr.responseText);
                        if (xhr.readyState === 4 && xhr.status === 200) {
                            let result = item.parseResult(d);
                            innerHTML = innerHTML.replace('{{result' + item.CODE + '}}', result);
                        } else if (xhr.status !== 200) {
                            innerHTML = innerHTML.replace('{{result' + item.CODE + '}}', 'ERROR: ' + JSON.stringify(xhr));
                        }
                        translateDiv.innerHTML = innerHTML;
                        item.status = true;
                        let allFinish = true;
                        for (let item of transList) {
                            allFinish = allFinish && item.status;
                        }
                        if (allFinish) {
                            translateDiv.style.display = "block";
                        }
                    }
                });
            }
        }
    }

    showIcon.onmouseup = function (ev) {
        ev = ev || window.event;
        ev.cancelBubble = true;
    };

    // 点击页面隐藏弹出框
    document.onclick = function (ev) {
        showIcon.style.display = 'none';
        translateDiv.style.display = 'none';
    };

    // 阻止事件冒泡，防止点击翻译框后隐藏
    translateDiv.onclick = function (ev) {
        event.stopPropagation();
    };

    // 获取随机数字字符串
    function getSalt() {
        let salt = "";
        for (let i = 0; i < 5; i++) salt += parseInt(Math.random() * 8);
        return salt;
    }

    /**
     * @return {string}
     */
    var MD5 = function (string) {

        function RotateLeft(lValue, iShiftBits) {
            return (lValue << iShiftBits) | (lValue >>> (32 - iShiftBits));
        }

        function AddUnsigned(lX, lY) {
            var lX4, lY4, lX8, lY8, lResult;
            lX8 = (lX & 0x80000000);
            lY8 = (lY & 0x80000000);
            lX4 = (lX & 0x40000000);
            lY4 = (lY & 0x40000000);
            lResult = (lX & 0x3FFFFFFF) + (lY & 0x3FFFFFFF);
            if (lX4 & lY4) {
                return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
            }
            if (lX4 | lY4) {
                if (lResult & 0x40000000) {
                    return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
                } else {
                    return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
                }
            } else {
                return (lResult ^ lX8 ^ lY8);
            }
        }

        function F(x, y, z) {
            return (x & y) | ((~x) & z);
        }

        function G(x, y, z) {
            return (x & z) | (y & (~z));
        }

        function H(x, y, z) {
            return (x ^ y ^ z);
        }

        function I(x, y, z) {
            return (y ^ (x | (~z)));
        }

        function FF(a, b, c, d, x, s, ac) {
            a = AddUnsigned(a, AddUnsigned(AddUnsigned(F(b, c, d), x), ac));
            return AddUnsigned(RotateLeft(a, s), b);
        }

        function GG(a, b, c, d, x, s, ac) {
            a = AddUnsigned(a, AddUnsigned(AddUnsigned(G(b, c, d), x), ac));
            return AddUnsigned(RotateLeft(a, s), b);
        }

        function HH(a, b, c, d, x, s, ac) {
            a = AddUnsigned(a, AddUnsigned(AddUnsigned(H(b, c, d), x), ac));
            return AddUnsigned(RotateLeft(a, s), b);
        }

        function II(a, b, c, d, x, s, ac) {
            a = AddUnsigned(a, AddUnsigned(AddUnsigned(I(b, c, d), x), ac));
            return AddUnsigned(RotateLeft(a, s), b);
        }

        function ConvertToWordArray(string) {
            var lWordCount;
            var lMessageLength = string.length;
            var lNumberOfWords_temp1 = lMessageLength + 8;
            var lNumberOfWords_temp2 = (lNumberOfWords_temp1 - (lNumberOfWords_temp1 % 64)) / 64;
            var lNumberOfWords = (lNumberOfWords_temp2 + 1) * 16;
            var lWordArray = Array(lNumberOfWords - 1);
            var lBytePosition = 0;
            var lByteCount = 0;
            while (lByteCount < lMessageLength) {
                lWordCount = (lByteCount - (lByteCount % 4)) / 4;
                lBytePosition = (lByteCount % 4) * 8;
                lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount) << lBytePosition));
                lByteCount++;
            }
            lWordCount = (lByteCount - (lByteCount % 4)) / 4;
            lBytePosition = (lByteCount % 4) * 8;
            lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80 << lBytePosition);
            lWordArray[lNumberOfWords - 2] = lMessageLength << 3;
            lWordArray[lNumberOfWords - 1] = lMessageLength >>> 29;
            return lWordArray;
        }

        function WordToHex(lValue) {
            var WordToHexValue = "", WordToHexValue_temp = "", lByte, lCount;
            for (lCount = 0; lCount <= 3; lCount++) {
                lByte = (lValue >>> (lCount * 8)) & 255;
                WordToHexValue_temp = "0" + lByte.toString(16);
                WordToHexValue = WordToHexValue + WordToHexValue_temp.substr(WordToHexValue_temp.length - 2, 2);
            }
            return WordToHexValue;
        }

        function Utf8Encode(string) {
            string = string.replace(/\r\n/g, "\n");
            let utftext = "";
            for (var n = 0; n < string.length; n++) {

                var c = string.charCodeAt(n);

                if (c < 128) {
                    utftext += String.fromCharCode(c);
                } else if ((c > 127) && (c < 2048)) {
                    utftext += String.fromCharCode((c >> 6) | 192);
                    utftext += String.fromCharCode((c & 63) | 128);
                } else {
                    utftext += String.fromCharCode((c >> 12) | 224);
                    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                    utftext += String.fromCharCode((c & 63) | 128);
                }

            }
            return utftext;
        }

        let x = Array();
        let k, AA, BB, CC, DD, a, b, c, d;
        let S11 = 7, S12 = 12, S13 = 17, S14 = 22;
        let S21 = 5, S22 = 9, S23 = 14, S24 = 20;
        let S31 = 4, S32 = 11, S33 = 16, S34 = 23;
        let S41 = 6, S42 = 10, S43 = 15, S44 = 21;

        string = Utf8Encode(string);

        x = ConvertToWordArray(string);

        a = 0x67452301;
        b = 0xEFCDAB89;
        c = 0x98BADCFE;
        d = 0x10325476;

        for (k = 0; k < x.length; k += 16) {
            AA = a;
            BB = b;
            CC = c;
            DD = d;
            a = FF(a, b, c, d, x[k + 0], S11, 0xD76AA478);
            d = FF(d, a, b, c, x[k + 1], S12, 0xE8C7B756);
            c = FF(c, d, a, b, x[k + 2], S13, 0x242070DB);
            b = FF(b, c, d, a, x[k + 3], S14, 0xC1BDCEEE);
            a = FF(a, b, c, d, x[k + 4], S11, 0xF57C0FAF);
            d = FF(d, a, b, c, x[k + 5], S12, 0x4787C62A);
            c = FF(c, d, a, b, x[k + 6], S13, 0xA8304613);
            b = FF(b, c, d, a, x[k + 7], S14, 0xFD469501);
            a = FF(a, b, c, d, x[k + 8], S11, 0x698098D8);
            d = FF(d, a, b, c, x[k + 9], S12, 0x8B44F7AF);
            c = FF(c, d, a, b, x[k + 10], S13, 0xFFFF5BB1);
            b = FF(b, c, d, a, x[k + 11], S14, 0x895CD7BE);
            a = FF(a, b, c, d, x[k + 12], S11, 0x6B901122);
            d = FF(d, a, b, c, x[k + 13], S12, 0xFD987193);
            c = FF(c, d, a, b, x[k + 14], S13, 0xA679438E);
            b = FF(b, c, d, a, x[k + 15], S14, 0x49B40821);
            a = GG(a, b, c, d, x[k + 1], S21, 0xF61E2562);
            d = GG(d, a, b, c, x[k + 6], S22, 0xC040B340);
            c = GG(c, d, a, b, x[k + 11], S23, 0x265E5A51);
            b = GG(b, c, d, a, x[k + 0], S24, 0xE9B6C7AA);
            a = GG(a, b, c, d, x[k + 5], S21, 0xD62F105D);
            d = GG(d, a, b, c, x[k + 10], S22, 0x2441453);
            c = GG(c, d, a, b, x[k + 15], S23, 0xD8A1E681);
            b = GG(b, c, d, a, x[k + 4], S24, 0xE7D3FBC8);
            a = GG(a, b, c, d, x[k + 9], S21, 0x21E1CDE6);
            d = GG(d, a, b, c, x[k + 14], S22, 0xC33707D6);
            c = GG(c, d, a, b, x[k + 3], S23, 0xF4D50D87);
            b = GG(b, c, d, a, x[k + 8], S24, 0x455A14ED);
            a = GG(a, b, c, d, x[k + 13], S21, 0xA9E3E905);
            d = GG(d, a, b, c, x[k + 2], S22, 0xFCEFA3F8);
            c = GG(c, d, a, b, x[k + 7], S23, 0x676F02D9);
            b = GG(b, c, d, a, x[k + 12], S24, 0x8D2A4C8A);
            a = HH(a, b, c, d, x[k + 5], S31, 0xFFFA3942);
            d = HH(d, a, b, c, x[k + 8], S32, 0x8771F681);
            c = HH(c, d, a, b, x[k + 11], S33, 0x6D9D6122);
            b = HH(b, c, d, a, x[k + 14], S34, 0xFDE5380C);
            a = HH(a, b, c, d, x[k + 1], S31, 0xA4BEEA44);
            d = HH(d, a, b, c, x[k + 4], S32, 0x4BDECFA9);
            c = HH(c, d, a, b, x[k + 7], S33, 0xF6BB4B60);
            b = HH(b, c, d, a, x[k + 10], S34, 0xBEBFBC70);
            a = HH(a, b, c, d, x[k + 13], S31, 0x289B7EC6);
            d = HH(d, a, b, c, x[k + 0], S32, 0xEAA127FA);
            c = HH(c, d, a, b, x[k + 3], S33, 0xD4EF3085);
            b = HH(b, c, d, a, x[k + 6], S34, 0x4881D05);
            a = HH(a, b, c, d, x[k + 9], S31, 0xD9D4D039);
            d = HH(d, a, b, c, x[k + 12], S32, 0xE6DB99E5);
            c = HH(c, d, a, b, x[k + 15], S33, 0x1FA27CF8);
            b = HH(b, c, d, a, x[k + 2], S34, 0xC4AC5665);
            a = II(a, b, c, d, x[k + 0], S41, 0xF4292244);
            d = II(d, a, b, c, x[k + 7], S42, 0x432AFF97);
            c = II(c, d, a, b, x[k + 14], S43, 0xAB9423A7);
            b = II(b, c, d, a, x[k + 5], S44, 0xFC93A039);
            a = II(a, b, c, d, x[k + 12], S41, 0x655B59C3);
            d = II(d, a, b, c, x[k + 3], S42, 0x8F0CCC92);
            c = II(c, d, a, b, x[k + 10], S43, 0xFFEFF47D);
            b = II(b, c, d, a, x[k + 1], S44, 0x85845DD1);
            a = II(a, b, c, d, x[k + 8], S41, 0x6FA87E4F);
            d = II(d, a, b, c, x[k + 15], S42, 0xFE2CE6E0);
            c = II(c, d, a, b, x[k + 6], S43, 0xA3014314);
            b = II(b, c, d, a, x[k + 13], S44, 0x4E0811A1);
            a = II(a, b, c, d, x[k + 4], S41, 0xF7537E82);
            d = II(d, a, b, c, x[k + 11], S42, 0xBD3AF235);
            c = II(c, d, a, b, x[k + 2], S43, 0x2AD7D2BB);
            b = II(b, c, d, a, x[k + 9], S44, 0xEB86D391);
            a = AddUnsigned(a, AA);
            b = AddUnsigned(b, BB);
            c = AddUnsigned(c, CC);
            d = AddUnsigned(d, DD);
        }
        let temp = WordToHex(a) + WordToHex(b) + WordToHex(c) + WordToHex(d);
        return temp.toLowerCase();
    }
})();
