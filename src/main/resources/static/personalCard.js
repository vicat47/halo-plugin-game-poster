class PersonalCardComponent extends HTMLElement {
    static observedAttributes = ["avatar", "avatar-mask", "username", "profile-background"];
    constructor() {
        super();
        let shadowRoot = this.attachShadow({mode: 'open'});
        shadowRoot.innerHTML = `
        <style>
            :host {
                display: flex;
                flex-direction: column;
                align-items: center;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
                max-width: 25vw;
                overflow: hidden;
            }
            
            .container {
                position: relative;
                width: 100%;
                height: 100%;
                z-index: 0;
            }
            
            .bg-media {
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                z-index: -1;
            }
            
            .bg-media>video {
                width: 100%;
                height: 100%;
                object-fit: cover;
                /*z-index: -1;*/
            }
            
            .header {
                display: flex;
                flex-direction: row;
                align-items: flex-start;
                width: 100%;
            }
            
            .avatar {
                position: relative;
            }
            
            .avatar #avatar-img {
                width: 80px;
                height: 80px;
                margin: 10px 10px;
            }
            
            .avatar #avatar-mask {
                position: absolute;
                left: 0;
                top: 0;
                width: 100px;
                height: 100px;
            }
            
            .info {
                text-align: left;
                margin-top: 10px;
                margin-left: 5px;
                flex-grow: 1;
            }
            
            .info h2 {
                margin: 0;
                font-size: 24px;
                color: #00b0f4;
            }
            
            .info .status {
                font-size: 16px;
                color: #43b581;
            }
            
            .details {
                display: flex;
                flex-direction: column;
                width: 100%;
                margin-top: 20px;
            }
            
            .detail {
                display: flex;
                justify-content: space-between;
                margin-bottom: 10px;
            }
            
            .detail .label {
                font-size: 14px;
                color: #99aab5;
            }
            
            .detail .value {
                font-size: 18px;
                color: #7289da;
            }
            
            .detail .xp {
                font-size: 12px;
                color: #99aab5;
                text-align: right;
            }

        </style>
        <div class="container">
            <div class="bg-media">
                <video autoplay loop muted>
                    <source src=""  type="video/mp4">
                </video>
            </div>
            <div class="header">
                <div class="avatar">
                    <img id="avatar-img" src="" alt="User Avatar">
                    <img id="avatar-mask" src="" alt="cover">
                </div>
                <div class="info">
                    <h2 id="username">vicat</h2>
                    <span class="status">在线</span>
                </div>
            </div>
            <div class="details">
                <div class="detail">
                    <span class="label">帐户服务年资</span>
                    <span class="value">9</span>
                    <span class="xp">450 点经验值</span>
                </div>
                <div class="detail">
                    <span class="label">Steam 等级</span>
                    <span class="value">18</span>
                </div>
            </div>
        </div>
        `
        shadowRoot.getElementById('avatar-img').setAttribute('src', this.getAttribute("avatar"));
        shadowRoot.getElementById('avatar-mask').setAttribute('src', this.getAttribute("avatar-mask"));
        shadowRoot.getElementById('username').innerText = this.getAttribute("username");
    }
    attributeChangedCallback(name, oldValue, newValue) {
        switch (name) {
            case "avatar": this.shadowRoot.getElementById('avatar-img').setAttribute("src", newValue); break;
            case "avatar-mask": this.shadowRoot.getElementById('avatar-mask').setAttribute("src", newValue); break;
            case "username": this.shadowRoot.getElementById('username').innerText = this.getAttribute("username"); break;
            case "profile-background": this.updateProfileBackground(newValue); break;
        }
    }

    updateProfileBackground(url) {
        let videoElement = this.shadowRoot.querySelector('.bg-media>video');
        videoElement.querySelector('source').setAttribute("src", url);
        videoElement.load();
    }
}

customElements.define("personal-card", Object.freeze(PersonalCardComponent));