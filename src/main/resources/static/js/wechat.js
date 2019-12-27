if (jQuery) {
    jQuery(function () {
        let jsApiList = ['onMenuShareAppMessage', 'onMenuShareTimeline', 'previewImage'];
        let href = window.location.href;
        let params = href.split("?")[1];
        let activityId = null, openId = null;
        for (let item of params.split("&")) {
            if (item.indexOf('activityId') !== -1) {
                activityId = item.substring('activityId='.length);
            }
            if (item.indexOf('openId') !== -1) {
                openId = item.substring('openId='.length);
            }
        }
        if (activityId) {
            jQuery.ajax({
                type: 'post',
                contentType: 'application/json',
                url: '/initWechatConfig',
                data: JSON.stringify({
                    url: href.split('#')[0],
                    activityId: activityId,
                    openId: openId
                }),
                dataType: 'json',
                success: function (result) {
                    alert(JSON.stringify(result))
                    let wechatConfig = result.wechatConfig;
                    if (wechatConfig.needFollow === 1 && result.isFollowed !== 2) {
                        // 在此处展示关注公众号弹窗，不可关闭
                        let bodyWidth = window.getComputedStyle(window.document.body).width;
                        let halfWidth = Number(('' + bodyWidth).replace(/px/g, '')) / 2;
                        let div = document.createElement('div');
                        let textHeight = halfWidth / 5;
                        let padding = halfWidth / 12;
                        div.innerHTML = `
                        <div id="wechatQrCodeMask" style="">
                            <div id="wechatQrCode" style="padding: 0 ${padding}px;">
                                <div style="height: ${textHeight + 'px'};line-height: ${textHeight + 'px'};">请先关注公众号</div>
                                <img id="wechatQrCodeImg" style="width: ${halfWidth + 'px'};" src="${wechatConfig.qrCodePath}" alt="qrcode" />
                                <div style="height: ${textHeight + 'px'};line-height: ${textHeight + 'px'};">关注后才能参与活动</div>
                            </div>
                        </div>
                    `;
                        document.body.appendChild(div);
                        let qrCodeDiv = document.getElementById('wechatQrCode');
                        let qrCodeDivWidth = Number((window.getComputedStyle(qrCodeDiv).width + '').replace(/px/g, ''));
                        qrCodeDiv.style.height = qrCodeDivWidth + textHeight * 2 + 'px';
                        let width = qrCodeDivWidth / 2 + padding;
                        qrCodeDiv.style.marginLeft = -width + 'px';
                        qrCodeDiv.style.marginTop = -(width + textHeight) + 'px';
                        vue.$el.style.filter = 'blur(5px) brightness(0.3)';
                        vue.$el.style.transform = 'scale(1.02)';
                        vue.$el.style.overflow = 'hidden';
                        vue.$el.style.width = '100%';
                        vue.$el.style.position = 'fixed';
                        let img = document.getElementById('wechatQrCodeImg');
                        img.addEventListener('dblclick', (e) => {
                            wx.previewImage({
                                current: wechatConfig.qrCodePath, // 当前显示图片的http链接
                                urls: [wechatConfig.qrCodePath] // 需要预览的图片http链接列表
                            });
                        });
                    }
                    if (wechatConfig.shareType === 2) {
                        wx.config({
                            debug: true,
                            appId: result.signatures.appId,
                            timestamp: result.signatures.timestamp,
                            nonceStr: result.signatures.nonceStr,
                            signature: result.signatures.signature,
                            jsApiList: jsApiList
                        });
                        wx.ready(function () {
                            wx.onMenuShareTimeline({
                                title: wechatConfig.shareTitle,
                                imgUrl: wechatConfig.shareLogoPath,
                                link: wechatConfig.shareUrl,
                                success: function () {

                                }
                            });
                            wx.onMenuShareAppMessage({
                                title: wechatConfig.shareTitle,
                                desc: wechatConfig.shareDesc,
                                imgUrl: wechatConfig.shareLogoPath,
                                link: wechatConfig.shareUrl,
                                type: 'link',
                                dataUrl: '',
                                success: function () {

                                }
                            })
                        });
                        wx.error = function (res) {
                            alert(JSON.stringify(res))
                        }
                    }
                }
            });
        }
    });
}
