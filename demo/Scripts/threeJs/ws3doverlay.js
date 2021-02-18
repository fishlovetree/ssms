/*
  * 创建设备弹出框标注
  *	@param {Array || undefined} options.position 三维坐标中的位置比如[0,0,0]
  *	@param {DOM || undefined} options.target 放置dom的位置
  *	@param {string || undefined} options.etype 设备类型
  *	@param {string || undefined} options.text 标注名称
  * */
var ws3doverlay = function (options) {
    var position = options.position ? options.position : [0, 0, 0];
    this.screenPosition = threeToScreenPos(position, camera);
    this.target_ = options.target ? options.target : document.body;
    this.equipmentType_ = options.etype ? options.etype : "";
    var text = options.text ? options.text : "";

    this.div_ = document.createElement("div");
    this.div_.innerHTML = "<span class=\"devicelabel\">" + text + "</span>";
    this.div_.className = "labeltip";
    this.target_.appendChild(this.div_);

    this.width = this.div_.offsetWidth;	//offsetWidth offsetHeight
    this.height = this.div_.offsetHeight;	//offsetWidth offsetHeight
    this.div_.style.top = this.screenPosition.y - 2 * this.height - 10 + "px";
    this.div_.style.left = this.screenPosition.x - this.width / 2 + "px";
    this.position_ = position;
}

//获取弹出框标注的三维世界坐标
ws3doverlay.prototype.getThreePosition = function () {
    return new THREE.Vector3(this.position_.x, this.position_.y, this.position_.z);
}

//更新弹出框位置
ws3doverlay.prototype.updatePosition = function () {
    var position = this.position_ ? this.position_ : { x: 0, y: 0 };

    this.screenPosition = threeToScreenPos(position, camera);

    this.div_.style.top = this.screenPosition.y - 2 * this.height - 10 + "px";
    this.div_.style.left = this.screenPosition.x - this.width / 2 + "px";
}

//显示
ws3doverlay.prototype.show = function () {
    this.div_.style.display = "block";
}

//获取可见性
ws3doverlay.prototype.getVisible = function () {
    var visible = true;
    if (this.div_.style.display == "none") {
        visible = false;
    }
    return visible;
}

//获取标注的设备类型，用于实时监控过滤
ws3doverlay.prototype.getEType = function () {
    return this.equipmentType_;
}

//隐藏
ws3doverlay.prototype.hide = function () {
    this.div_.style.display = "none";
}

//获取屏幕位置坐标
ws3doverlay.prototype.getScreenPosition = function () {
    return {
        x: this.screenPosition.x,
        y: this.screenPosition.y - 2 * this.height - 10
    }
}

//获取弹出坐标的尺寸，为了进行相交判断
ws3doverlay.prototype.getSize = function () {			//获取尺寸
    return {
        width: this.width,
        height: this.height
    }
}

//清除弹出标注
ws3doverlay.prototype.clear = function () {
    var div = this.div_ ? this.div_ : null;
    this.target_.removeChild(div);
}


/*
 * 创建设备状态,在模拟演练中用到
  *	@param {Array || undefined} options.position 三维坐标中的位置比如[0,0,0]
  *	@param {DOM || undefined} options.target 放置dom的位置
  *	@param {string || undefined} options.buildingid 建筑id
  *	@param {string || undefined} options.name 名称
  *	@param {string || undefined} options.text 状态内容
  * */
var ws3dstatus = function (options) {
    var position = options.position ? options.position : [0, 0, 0];
    var screenPosition = threeToScreenPos(position, camera);
    this.target_ = options.target ? options.target : document.body
    this.buildingid_ = options.buildingid ? options.buildingid : undefined;
    this.name_ = options.name ? options.name : undefined;
    var text = options.text ? options.text : "";

    this.div_ = document.createElement("div");
    this.div_.innerHTML = "<span class=\"devicelabel\">" + text + "</span>";
    this.div_.className = "statustip";
    this.target_.appendChild(this.div_);

    var offsetHeight = this.div_.offsetHeight;	//offsetWidth offsetHeight
    this.div_.style.top = screenPosition.y - 25 + "px";
    this.div_.style.left = screenPosition.x + 14 + "px";
    this.position_ = position;
}

//更新设备状态的位置
ws3dstatus.prototype.updatePosition = function () {
    this.show();

    var position = this.position_ ? this.position_ : { x: 0, y: 0 };

    var screenPosition = threeToScreenPos(position, camera);

    var offsetHeight = this.div_.offsetHeight;	//offsetWidth offsetHeight
    this.div_.style.top = screenPosition.y - 25 + "px";
    this.div_.style.left = screenPosition.x + 14 + "px";
}

//隐藏
ws3dstatus.prototype.hide = function () {
    this.div_.style.display = "none";
}

//显示
ws3dstatus.prototype.show = function () {
    this.div_.style.display = "block";
}

//获取楼层id
ws3dstatus.prototype.getbuildingid = function () {
    return this.buildingid_;
}

//获取名称
ws3dstatus.prototype.getName = function () {
    return this.name_;
}

//清除
ws3dstatus.prototype.clear = function () {
    var div = this.div_ ? this.div_ : null;
    this.target_.removeChild(div);
}