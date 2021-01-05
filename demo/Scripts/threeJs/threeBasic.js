var scene,camera,renderer;      //场景、相机、渲染、光源
var container;				//三维容器
var controls;

var labelGroup;			//用于存放标注的组
var customerGroup;		//楼层客户组
var floorGroup;         						//楼层组
var floorItem;           //具体楼层
var center = {x: 3390453.5408852194,y: 0,z:13474270.229348525 };             //中心点
var drawingPlane;       						//绘制的平面区域

var customerOverlayArray = [];						//保存所有的客户标注
var noShowType = [];    							//保存不显示的设备类型，用以筛选
var cameraOffset;			//每个建筑的offset都不一样

var customerAlarmArray = [];		//告警客户的集合
var customerAlarmParam = 0;		//客户告警的参数，保证所有告警频率一致

var popPosition; //popup框位置

//楼层子项的分类类型
var ObjType = {
    CUBE:"cube",
    FLOOR:"floor",          //地板
    CELL:"cell",            //常用的小隔间
    WALL:"wall",            //墙体
    GLASS:"glass"          //玻璃
}

//颜色常量 包括填充颜色和边框颜色
var colorConst = [
    {fill:"#F8D3A5",stroke:"#F7A540"},          //橙色
    {fill:"#f7dee4",stroke:"#EEAEEE"},          //粉红色，
    {fill:"#bfdaf7",stroke:"#99ccff"},          //蓝色
    {fill:"#ece4d8",stroke:"#D2B48C"}           //土色
]

//标注图片的路径
var labelPicUrl = {			
	1:"/img/stairs1.png",					//楼梯图标
	2:"/img/elevator1.png",				//电梯图标
	3:"/img/wc1.png",						//厕所图标
	4:"/img/goodelevator1.png",			//货梯图标
	"default":"/img/default.png"			//默认的图标
}

//客户图标
var DEVIMGURL = {
	defaultP:"/img/defaultP.png",				//默认图标
	defaultPAlarm: "/img/defaultPAlarm.png",
}

var threeBasic = {
    //整体初始化
    init3D(domId){
        this.initContainer(domId);
        this.initScene();
        this.initCamera();
        this.initLight();
        this.initRender();
        this.initControls();
    },
    //初始化三维容器
    initContainer(domId){
        if(domId){
            container = document.getElementById(domId);
        }else{
            container = document.body;
        }
    },
    //初始化场景
    initScene(){
        scene = new THREE.Scene();
        scene.background = new THREE.Color( 0x212d39 );		//背景颜色
        //scene.fog = new THREE.Fog( 0x72645b);//雾效果 	颜色、最短距离、最长距离
    },
    //初始化相机
    initCamera(){
        var aspect = container.clientWidth/container.clientHeight;	//纵横比
        camera = new THREE.PerspectiveCamera( 40, aspect, 1, 1000);//第一个参数为渲染角度，人眼一般设置为40-50
    },
    //初始化光照
    initLight() {
        var ambientLight = new THREE.AmbientLight( 0xffffff, 0.5 );		//环境光
        scene.add( ambientLight );
        
        var directionalLight = new THREE.DirectionalLight( 0xffffff, 0.4 );		//直射光 模拟日光  l
        directionalLight.position.set( 1, 1, 0 ).normalize();
        scene.add( directionalLight );
        
        var directionalLight2 = new THREE.DirectionalLight( 0xffffff, 0.4 );		//直射光 模拟日光
        directionalLight2.position.set( 0, -1, -1 ).normalize();
        scene.add( directionalLight2 );
    },
    //初始化渲染
    initRender(){
        renderer = new THREE.WebGLRenderer({antialias: true});
        renderer.setSize( container.clientWidth, container.clientHeight );
        container.appendChild( renderer.domElement );
        renderer.setClearColor(0xFFFFFF, 1.0);
    },
    //初始化用户交互
    initControls(){
        controls = new THREE.OrbitControls( camera,renderer.domElement );//用户交互 翻转
        controls.zoomSpeed = 3;
        controls.maxPolarAngle = Math.PI/2;	//设置相机的角度范围 最大45度 Math.PI/4
        controls.minPolarAngle = 0;//设置相机的角度范围
        controls.minDistance = 5;         //相机距离目标点的最近距离
        controls.maxDistance = 200;         //相机距离目标点的最远距离
        controls.enableRotate = true;
        //controls.enablePan = true; 
        controls.addEventListener('change',function(){			//用户交互改变事件

        });
    },
    //更新交互控制
    updateControls() {
        controls.update();
    },
    //刷新三维渲染
    refreshThreeWindow(){
        var HEIGHT = container.clientHeight;		// 更新渲染器的高度和宽度以及相机的纵横比
        var WIDTH = container.clientWidth;
        camera.aspect = WIDTH / HEIGHT;
        camera.updateProjectionMatrix();
        renderer.setSize( WIDTH, HEIGHT );
    },
    //初始化三维对象
    init3DObject(){
        labelGroup = new THREE.Object3D();			//初始化标注组
        scene.add(labelGroup);
        
        customerGroup = new THREE.Object3D();				//初始化楼层设备组
        scene.add(customerGroup);
        
        floorGroup = new THREE.Object3D();				//初始化楼层模型组
        scene.add(floorGroup);
    },
    //三维页面窗口改变处理
    handleWindowResize() {
        if(container.style.display == "none") return;			//针对三维建筑，处理窗口改变事件
        var HEIGHT = container.clientHeight;		// 更新渲染器的高度和宽度以及相机的纵横比
        var WIDTH = container.clientWidth;
        camera.aspect = WIDTH / HEIGHT;
        camera.updateProjectionMatrix();
        renderer.setSize( WIDTH, HEIGHT );
        this.updatePopPosition();		//更新弹出框的位置
    },
    //三维页面窗口改变处理
    resizeThreeWindow(width,height) {
        camera.aspect = width / height;
        camera.updateProjectionMatrix();
        renderer.setSize( width, height );
        this.updatePopPosition();		//更新弹出框的位置
    },
    /*
    * 为客户添加dom类型的标注
    * @param {String} text 标注内容
    * @param {THREE.Vector3} position 客户位置点
    * */
    addCustomerLabelToMap(text,position){
        if(!(position instanceof THREE.Vector3)) return;
        var threePosition = [position.x,position.y,position.z];
        var customerOverlay = new ws3doverlay({
            target:container,
            position:threePosition,
            text:text ? text : ""
        })
        customerOverlayArray.push(customerOverlay);
    },
    //更新外部标注的位置 和相交判断
    updateCustomerLabel(){
        if(!customerOverlayArray || customerOverlayArray.length == 0) return;
        for(var i=0;i<customerOverlayArray.length;i++){
            var customerOverlay = customerOverlayArray[i];
            var level = this.getPointLevel(customerOverlay.getThreePosition());
            if(level>2.8){			//模拟显示级别
                customerOverlay.hide();
            }else{
                if(!customerOverlay.getVisible()){
                    customerOverlay.show();
                }
            }
        }
        for(var k=0;k<customerOverlayArray.length;k++){		//设备标注集合 相交判断
            var comparedOverlay = customerOverlayArray[k];
            comparedOverlay.updatePosition();
            if(comparedOverlay.getVisible()){
                for(var m=k+1;m<customerOverlayArray.length;m++){
                    var customerOverlay1 = customerOverlayArray[m];
                    if(this.isCustomerLabelRect(comparedOverlay,customerOverlay1)){
                        customerOverlay1.hide();
                    }
                }
            }
        }
    },
    /*
    *	验证两个标注是否相交 
    *  @param {ws3doverlay} a 标注a
    *  @param {ws3doverlay} b 标注b
    *  @return {boolean} true则相交
    */
    isCustomerLabelRect(a,b){
        if(!(a instanceof ws3doverlay) || !(b instanceof ws3doverlay)) return true;
        var acenter = a.getScreenPosition(),asize = a.getSize();
        var bcenter = b.getScreenPosition(),bsize = b.getSize();
        if(!acenter || !asize || !bcenter || !bsize) return true;
        var w1 = asize.width,h1 = asize.height;
        var x1 = acenter.x - w1/2,y1 = acenter.y - h1/2;
        
        var w2 = bsize.width,h2 = bsize.height;
        var x2 = bcenter.x - w2/2,y2 = bcenter.y - h2/2;
        
        if (x1 >= x2 && x1 >= x2 + w2) {
            return false;
        } else if (x1 <= x2 && x1 + w1 <= x2) {
            return false;
        } else if (y1 >= y2 && y1 >= y2 + h2) {
            return false;
        } else if (y1 <= y2 && y1 + h1 <= y2) {
            return false;
        }else{
            return true;
        }
    },
    /*
    *	添加绘制平面，辅助绘制
    *	@param {Array} position [x,y,z,w]
    */
    addDrawingPlane(position){
        var height = position[2],buildHeight = position[3];		//楼的基点高度、和建筑厚度
        if(!drawingPlane){
            var geometry = new THREE.PlaneGeometry( 1000,1000,0.1);
            var material = new THREE.MeshBasicMaterial( {color: 0xffffff,transparent:true,opacity:0.0,side: THREE.DoubleSide} );
            drawingPlane = new THREE.Mesh( geometry, material );
            drawingPlane.position.x = 0;
            drawingPlane.position.y = height + buildHeight;
            drawingPlane.position.z = 0;
            drawingPlane.rotation.x = - Math.PI / 2;
            scene.add( drawingPlane );
        }else{
            drawingPlane.position.y = height + buildHeight;
        }
    },
    //删除绘制平面
    removeDrawingPlane(){
        if(drawingPlane){
            scene.remove(drawingPlane);
            drawingPlane = undefined;
        }
    },
    //清除一个建筑的通用要素
    clearBuilding(){
        if(floorGroup){
            scene.remove(floorGroup);		
            floorGroup = new THREE.Object3D();
            scene.add(floorGroup);								//清除楼层
        }
        if(customerGroup){
            scene.remove(customerGroup);			
            customerGroup = new THREE.Object3D();
            scene.add(customerGroup);								//清除设备图标
        }
        if(labelGroup){
            scene.remove(labelGroup);			
            labelGroup = new THREE.Object3D();
            scene.add(labelGroup);								//清除楼层标注
        }
        if(customerOverlayArray && customerOverlayArray.length != 0){
            for(var i=0;i<customerOverlayArray.length;i++){		//清除客户标注
                var customerOverlay = customerOverlayArray[i];
                customerOverlay.clear();
            }
            customerOverlayArray = [];
        }
        if($("#popup").css("display") != "none"){
            popPosition = undefined;
            $("#popup").hide();						//隐藏弹出框
        }
    },
    /*
    * 添加楼层
    * @param model 楼层对应的模型实体 
    * (model.coords模型中心点坐标；model.url模型路径；model.cameraposition模型相机点位置	model.cameratarget模型相机目标位置)
    * */
    addmodel(model, floorInfo){
        center = this.getThreeCenter(model.coords);
        var position = this.lonlatToThree(model.coords);		//经纬度转three	
        if(floorInfo instanceof Object){
            /*var floor = new WsFloor(floorInfo);
            floor.load(position.y);*/
            if(!floorGroup){
                console.log("组件初始化尚未完成！");
                return;
            }
            floorItem = new THREE.Object3D();		//floorGroup;  //存放楼层元素的容器
            floorGroup.add(floorItem);
            var items = floorInfo.buildingItems;
            for(var i=0;i < items.length;i++){
                var item = items[i];
                var type = item.type;
                var points = item.points;
                switch(type){
                    case ObjType.CELL:
                        var colorIndex = item.color;
                        this.addCell(points,2,colorIndex);
                        break;
                    case ObjType.WALL:
                        this.addWall(points,2.5);
                        break;
                    case ObjType.FLOOR:
                        this.addFloor(points,0.1);
                        break;
                }
            }
            floorItem.position.y = position.y;
        }
        var cameraposition = model.cameraposition ? model.cameraposition:undefined;
        var cameratarget = model.cameratarget ? model.cameratarget:undefined;
        this.adjustCamera(position,cameraposition,cameratarget); 
    },
    /*
    *	生成顶部的边线 
    *	@param {Array} points 底部坐标点集合
    *	@param {number} height 盒子的高度
    * 	@param {number} color 颜色序号
    */
    getBorderGeometry (points,height,color){
        var geometry = new THREE.Geometry();
        var topPoints = [];
        for(var i=0;i<points.length;i++){
            var vertice = points[i];
            topPoints.push([vertice[0],vertice[1]+height,vertice[2]]);
        }
        for(var i=0;i<topPoints.length;i++){
            var topPoint = topPoints[i];
            geometry.vertices.push(new THREE.Vector3(topPoint[0],topPoint[1],topPoint[2]));
            if(i==topPoints.length-1){
                geometry.vertices.push(new THREE.Vector3(topPoints[0][0],topPoints[0][1],topPoints[0][2]));
            }
        }
        return geometry;
    },
    /*
    *	根据传入的底部坐标点和高度，创建几何 
    *	@param {Array} points 底部坐标点集合
    *	@param {height} height 高度
    *	@return {THREE.Geometry} geometry
    */
    getGeometry(points,height){
        if(points.length < 3){
            console.log("至少需要三个点来创建盒子");
            return;
        }
        var topPoints = [];
        for(var i=0;i<points.length;i++){
            var vertice = points[i];
            topPoints.push([vertice[0],vertice[1]+height,vertice[2]]);
        }
        var totalPoints = points.concat(topPoints);
        var vertices =[];
        for(var i=0;i<totalPoints.length;i++){
            vertices.push(new THREE.Vector3(totalPoints[i][0],totalPoints[i][1],totalPoints[i][2]))
        }
        var length = points.length;
        var faces = [];
        for(var j=0;j<length;j++){                      //侧面生成三角形
            if(j!=length-1){
                faces.push(new THREE.Face3(j,j+1,length+j+1));
                faces.push(new THREE.Face3(length+j+1,length+j,j));
            }else{
                faces.push(new THREE.Face3(j,0,length));
                faces.push(new THREE.Face3(length,length+j,j));
            }
        }
        var data=[];
        for(var i=0;i<length;i++){
            data.push(points[i][0],points[i][2]);
        }
        var triangles = Earcut.triangulate(data);
        if(triangles && triangles.length != 0){
            for(var i=0;i<triangles.length;i++){
                var tlength = triangles.length;
                if(i%3==0 && i < tlength-2){
                    faces.push(new THREE.Face3(triangles[i],triangles[i+1],triangles[i+2]));                            //底部的三角面
                    var topface = new THREE.Face3(triangles[i]+length,triangles[i+1]+length,triangles[i+2]+length);
                    topface.materialIndex = 1;
                    faces.push(topface);        //顶部的三角面
                }
            }
        }
        var geometry = new THREE.Geometry();
        geometry.vertices = vertices;
        geometry.faces = faces;
        return geometry;
    },
    /*
    *   创建地板平面
    *   @param {Array} points 底部坐标集合
    *   @height {number} height 地板高度
    * */
    addFloor(points,height){
        var transPoints = [];
        for(var i=0;i<points.length;i++){
            var point = points[i];
            transPoints.push([-point[2],point[1],point[0]]);		//z轴是反的
        }
        var geometry = this.getGeometry(transPoints,height);
        geometry.computeFaceNormals();          //计算法向量
        var material = [
            new THREE.MeshLambertMaterial({color: "#eee",side:THREE.DoubleSide,blending:THREE.NoBlending}),
            new THREE.MeshBasicMaterial({color: "#eee",side:THREE.DoubleSide,blending:THREE.NoBlending})
        ]
        var mesh = new THREE.Mesh(geometry,material);
        mesh.castShadow = true;
        floorItem.add(mesh);				//添加填充
        if(controls) controls.minDistance = 30;
    },
    /*
    *   创建不规则的小室
    *   @param {Array} points 底部坐标点集合
    *   @param {number} height 高度
    *   @param {number} colorIndex 颜色在colorConst中的序号
    * */
    addCell(points,height,colorIndex){
        var transPoints = [];
        for(var i=0;i<points.length;i++){
            var point = points[i];
            transPoints.push([-point[2],point[1],point[0]]);		//z轴是反的
        }
        var geometry = this.getGeometry(transPoints,height);
        geometry.computeFaceNormals();          //计算法向量
        var material = [
            new THREE.MeshLambertMaterial({color:colorConst[colorIndex].fill,side:THREE.DoubleSide}),
            new THREE.MeshBasicMaterial({color:colorConst[colorIndex].fill,side:THREE.DoubleSide})		//不受光照影响
        ];
        var mesh = new THREE.Mesh(geometry,material);
        floorItem.add(mesh);				//添加填充

        var lineMaterial = new THREE.LineBasicMaterial({color:colorConst[colorIndex].stroke});
        var lineGeometry =this.getBorderGeometry(transPoints,height);
        var line = new THREE.Line(lineGeometry, lineMaterial);
        floorItem.add(line);
    },
    /*
    *   创建墙体
    *   @param {Array} points 底部坐标点集合
    *   @param {number} height 墙体的高度
    */
    addWall(points,height){
        var transPoints = [];
        for(var i=0;i<points.length;i++){
            var point = points[i];
            transPoints.push([-point[2],point[1],point[0]]);		//z轴是反的
        }
        var geometry = this.getGeometry(transPoints,height);
        geometry.computeFaceNormals();          //计算法向量
        var material = [
            new THREE.MeshLambertMaterial({color:'#fff',side:THREE.DoubleSide,blending:THREE.NoBlending}),
            new THREE.MeshBasicMaterial({color:'#fff',side:THREE.DoubleSide,blending:THREE.NoBlending})
        ]
        var mesh = new THREE.Mesh(geometry,material);
        floorItem.add(mesh);				//添加填充
    },
    /*
    *	根据楼层调整相机 
    *  @param {THREE.Vector3} position 目标点位置
    *  @param {Array} cameraposition 相机位置 [x,y,z]
    */
    adjustCamera(position,cameraposition,cameratarget){
        if(cameraposition && cameratarget){
            cameraOffset = [cameraposition[0]-cameratarget[0],cameraposition[1]-cameratarget[1],cameraposition[2]-cameratarget[2]];
            camera.position.y = position.y + cameraposition[1];
            camera.position.z = position.z + cameraposition[2];
            camera.position.x = position.x + cameraposition[0];
            var targetx = position.x+cameratarget[0],targety = position.y+cameratarget[1],targetz = position.z+cameratarget[2]
            camera.lookAt({x:targetx,y:targety,z:targetz});
            controls.target = new THREE.Vector3(targetx,targety,targetz);
        }else{
            cameraOffset = [0,10,0];
            camera.position.y = position.y + 10;
            camera.position.z = position.z;
            camera.position.x = position.x;
            camera.lookAt({x:position.x,y:position.y,z:position.z});
            controls.target = new THREE.Vector3(position.x,position.y,position.z); 
    }
    },
    /*
    *	定位到一个客户sprite
    *	@param  customerNum 客户编码 
    */
    flyToCustomer(customerNum){
        var customerSprite = customerGroup.getObjectByName("customer:" + customerNum);
 		if(customerSprite){
            var position = customerSprite.position;
            if(!position || !cameraOffset) return;
            camera.position.y = position.y + cameraOffset[1];
            camera.position.z = position.z + cameraOffset[2];
            camera.position.x = position.x + cameraOffset[0];
            camera.lookAt({x:position.x,y:position.y,z:position.z});
            controls.target = new THREE.Vector3(position.x,position.y,position.z);           //设置orbit交互的中心点
        }
    },
    /*
    * 添加楼层客户到地图上
    * @param {[Object]} data 客户数据组 
    * */
    addCustomersToMap(data){
        for(var i=0; i<data.length; i++){
            var dataItem = data[i];
            if(/^\[\d+(\.\d+)\,\d+(\.\d+)\,\d+(\.\d+)\]$/.test(dataItem.coordinate)){		//验证经纬度高度
                var coordinate = eval("("+ data[i].coordinate +")");
                var url = DEVIMGURL.defaultP;
                var sprite = this.makeIconSprite(url);
                sprite.renderOrder = 2;		//渲染顺序
                sprite.center.set( 0.5, 0 );
                sprite.name = 'customer:'+data[i].customerNum;
                sprite.userData = data[i];
                sprite.userData.type = "customer";
                var spritePos = this.lonlatToThree(coordinate);
                sprite.position.x = spritePos.x;
                sprite.position.y = spritePos.y;
                sprite.position.z = spritePos.z;
                customerGroup.add(sprite);			//添加客户到场景
            }
        }
        
        var customers = customerGroup.children ? customerGroup.children:[];		//所有的客户
        for(var j = 0;j < customers.length; j++){
            var sprite1 = customers[j];
            var text = sprite1.userData.customerName ? sprite1.userData.customerName : "";
            var position = sprite1.position;
            this.addCustomerLabelToMap(text,position);						//添加设备标注
        }
    },
    /*
    *	 创建楼层标注
    * 	@param {Object} model --model.label 模型标注文件的路径
    */
    createLabel(label){
        if (label instanceof Object){
            for(var i=0;i<label.length;i++) {
                var type = label[i].type;
                if(labelPicUrl[type]){
                    this.addIconSprite(labelPicUrl[type],label[i].position);
                }else{
                    this.addLabelSprite(label[i].text,label[i].position);
                }
            }
        }
    },
    /*
    * 根据图片url添加标注
    * @param {String} url 图片的路径
    * @param {Array} position 标注点的经纬度高度[lon,lat,height]
    */
    addIconSprite(url,position){
        var sprite = this.makeIconSprite(url);
        sprite.renderOrder = 0;
        var spritePos = this.lonlatToThree(position);
        sprite.center.set( 0.5, 0 );		//设置锚点
        sprite.position.x = spritePos.x;
        sprite.position.y = spritePos.y;
        sprite.position.z = spritePos.z;
        sprite.visible = false;
        labelGroup.add(sprite);
    },
    /*
    *	根据图片创建Sprite，通用方法
    *	@param {String} url 图片路径 
    *	@return {THREE.Sprite} sprite 图标sprite
    */
    makeIconSprite(url){
        var loader = new THREE.TextureLoader();
        var texture = loader.load(url);
        var spriteMaterial = new THREE.SpriteMaterial({ map: texture,depthWrite:false});
        var sprite = new THREE.Sprite(spriteMaterial);
        return sprite;
    },
    /*
    *	根据文字添加标注 
    *	@param {String} message 文字内容 
    *	@param {Array} position 经纬度高度
    */
    addLabelSprite(message,position){
        var sprite = this.makeTextSprite(message);
        sprite.renderOrder = 0;
        sprite.center = new THREE.Vector2(0.5,0);
        
        var spritePos = this.lonlatToThree(position);
        sprite.position.x = spritePos.x;
        sprite.position.y = spritePos.y;
        sprite.position.z = spritePos.z;
        sprite.visible = false; 
        labelGroup.add(sprite);
    },
    /*
    * 根据文字创建sprite
    * @param {String} message 文字内容
    * @return {THREE.Sprite} sprite 文字sprite
    */
    makeTextSprite(message){
        var textCanvas = this.makeTextCanvas(message);
        var texture = new THREE.Texture(textCanvas);
        texture.needsUpdate = true;
        var spriteMaterial = new THREE.SpriteMaterial({map : texture,depthWrite:false});
        var sprite = new THREE.Sprite(spriteMaterial);
        return sprite; 
    },
    /*
    * 根据文字绘制canvas图形
    * @param {String} text 文字内容
    * @return canvas canvas图形
    */
    makeTextCanvas(text) {
        var canvas = document.createElement( 'canvas' );
        var context = canvas.getContext( '2d' );
        
        context.font='50px Microsoft YaHei';
        canvas.width = context.measureText(text).width;      //根据文字内容获取宽度
        canvas.height = 58; // fontsize * 1.5

        context.beginPath();
        context.font='50px Microsoft YaHei';
        context.fillStyle = "#222";
        context.fillText(text,0,50);
        context.fill();
        context.stroke();
        return canvas;
    },
    /*
    *	获取POI标注sprite的合适比例
    *	@param {THREE.Vector3} position poi点的位置
    *  @param {Object} poiRect sprite的像素高度和宽度{w:width,h:height}
    *  @return {Array}	[scaleX,scaleY,1.0]
    */
    getPoiScale(position,poiRect){
        if(!position) return;
        var distance = camera.position.distanceTo(position);        //相机和标注点的距离
        var top = Math.tan(camera.fov / 2 * Math.PI / 180)*distance;    //camera.fov 相机的拍摄距离
        var meterPerPixel = 2*top/container.clientHeight;
        var scaleX = poiRect.w * meterPerPixel;
        var scaleY = poiRect.h * meterPerPixel;
        return [scaleX,scaleY,1.0];
    },
    /*	
    * 	获取设备Sprite的大小比例
    *	@param {THREE.Vector3} position poi点的位置
    *  @param {Object} poiRect sprite的像素高度和宽度{w:width,h:height}
    *  @return {Array}	[scaleX,scaleY,1.0] 
    */
   getCustomerScale(position,poiRect){			//1-2之间  20-200
        if(!position) return;
        var distance = camera.position.distanceTo(position);        //相机和标注点的距离
        var minDis = controls.minDistance,maxDis = controls.maxDistance;
        var level = 1+(distance - minDis)*6/(maxDis - minDis);		 //按照相机与点的距离，细分为7个级别
        
        var top = Math.tan(camera.fov / 2 * Math.PI / 180)*distance;    //camera.fov 相机的拍摄距离
        var meterPerPixel = 2*top/container.clientHeight;
        var scaleX = (1.2-level/14)*poiRect.w * meterPerPixel;
        var scaleY = (1.2-level/14)*poiRect.h * meterPerPixel;
        return [scaleX,scaleY,1.0];
    },
    /*
    * 	相机点与某位置的距离与 最大最小距离的比值,距离不足以判断
    * 	@param {THREE.Vector3} position sprite点的位置
    *	@return {number} 比值
    */
    getPointLevel(position){
        var distance = camera.position.distanceTo(position);        //相机和标注点的距离
        var minDis = controls.minDistance,maxDis = controls.maxDistance;
        return (distance - minDis)*6/(maxDis - minDis);
    },
    //更新文字sprite标注
    updateLabelSprite(){
        var sprites = labelGroup.children;
        if(sprites.length == 0) return; 
        for(var i=0;i<sprites.length;i++){
            var sprite = sprites[i];
            sprite.visible = true;
        }
        
        for(var j=0;j<sprites.length;j++){
            var compareSprite = sprites[j];
            var canvas = compareSprite.material.map.image;
            if(canvas){
                var position =  compareSprite.position;
                var scale = this.getPoiScale(position,{w:canvas.width,h:canvas.height});
                compareSprite.scale.set(scale[0]/4,scale[1]/4,1.0);
                if(compareSprite.visible){		//true
                    for(var m=j+1;m<sprites.length;m++){
                        var sprite1 = sprites[m];
                        if(this.isPOILabelRect(compareSprite,sprite1)){
                            sprite1.visible = false;
                        }
                    }
                }
            }
        }
    },
    /*
    *	检测两个标注sprite是否碰撞
    *  @param {THREE.Sprite} sprite1 标注sprite1
    *  @param {THREE.Sprite} sprite2 标注sprite2
    *  @return {boolean} true则碰撞 false不碰撞
    */
    isPOILabelRect(sprite1,sprite2){
        var comparePosition = this.threeToScreenPos([sprite1.position.x,sprite1.position.y,sprite1.position.z]);
        var spritePosition = this.threeToScreenPos([sprite2.position.x,sprite2.position.y,sprite2.position.z]);
        
        var image1 = sprite1.material.map.image;
        var image2 = sprite2.material.map.image;
        var w1 = image1.width/2;
        var h1 = image1.height/2;
        var x1 = comparePosition.x - w1/2;
        var y1 = comparePosition.y - h1/2;

        var w2 = image2?image2.width/2:0;
        var h2 = image2?image2.height/2:0;

        var x2 = spritePosition.x - w2/2;         //点2左下角的xy点
        var y2 = spritePosition.y - h2/2;

        if (x1 >= x2 && x1 >= x2 + w2) {
            return false;
        } else if (x1 <= x2 && x1 + w1 <= x2) {
            return false;
        } else if (y1 >= y2 && y1 >= y2 + h2) {
            return false;
        } else if (y1 <= y2 && y1 + h1 <= y2) {
            return false;
        }else{
            return true;
        }
    },
    //更新客户sprite尺寸
    updateCustomerSprite(){
        var sprites = customerGroup.children;
        if(!customerGroup || !sprites || sprites.length == 0) return;
        for(var i=0;i<sprites.length;i++){
            var sprite = sprites[i];
            var position =  controls.target;//sprite.position;
            var canvas = sprite.material.map.image;
            if(canvas){
                var scale = this.getCustomerScale(position,{w:canvas.width,h:canvas.height});
                sprite.scale.set(scale[0],scale[1],1.0);
                sprite.visible = true;	
            }
        }
    },
    /*
    *	测试三维点坐标是否在可视范围内
    * 	@param {Array} position 三维点坐标 [x,y,z]
    *  @return {boolean}
    */
    isOffScreen(position){
        var Vector = new THREE.Vector3(
                position[0],
                position[1],
                position[2]
        );
        var frustum = new THREE.Frustum(); //Frustum用来确定相机的可视区域
        var cameraViewProjectionMatrix = new THREE.Matrix4();
        cameraViewProjectionMatrix.multiplyMatrices(camera.projectionMatrix, camera.matrixWorldInverse); //获取相机的法线
        frustum.setFromMatrix(cameraViewProjectionMatrix); //设置frustum沿着相机法线方向

        return frustum.containsPoint(Vector);       //可视范围包含这个点
    },
    /*
    * 初始化三维中心点的墨卡托坐标，用以经纬度转三维世界坐标
    * @param {Array} coords 经纬度高度坐标
    * @return {Object} 中心点的三维坐标
    */
    getThreeCenter(coords){
        var z = coords[2]? coords[2]:0;
        var x = (coords[0] / 180.0) * 20037508.3427892;
        var y = (Math.PI / 180.0) * coords[1];
        var tmp = Math.PI / 4.0 + y / 2.0;
        y = 20037508.3427892 * Math.log(Math.tan(tmp)) / Math.PI;
        return {x: y,y: 0,z:x };
    },
    /*
    *	三维中这里使用Z轴对应实际的经度，Y轴对应实际的高度，X轴对应实际的纬度
    *   @param {Array} lonlat 经纬度高度 
    *   @return {Object} {x,y,z} 三维世界坐标
    */
    lonlatToThree(lonlat){
        var z = lonlat[2]? lonlat[2]:0;
        var x = (lonlat[0] / 180.0) * 20037508.3427892;
        var y = (Math.PI / 180.0) * lonlat[1];
        var tmp = Math.PI / 4.0 + y / 2.0;
        y = 20037508.3427892 * Math.log(Math.tan(tmp)) / Math.PI;
        var result = {
            x: y - center.x,
            y: z - center.y,
            z: x -center.z
        };
        return result;
    },
    /*
    *  三维世界坐标转经纬度坐标
    *  @param {THREE.Vector3} 
    *  @return {Array} [lon,lat,height]
    */
    threeToLonlat(three){
        var lng = (three.z + center.z)/20037508.34*180;
        var lat = (three.x + center.x)/20037508.34*180;
        lat= 180/Math.PI*(2*Math.atan(Math.exp(lat*Math.PI/180))-Math.PI/2);
        var height = three.y + center.y;
        return [lng, lat,height]; //[114.32894001591471, 30.58574800385281]  
    },
    /*
    *  判断一个点是否在多边形内部 
    *  @param {[[x1,y1],[x2,y2],[x3,y3]]}points 多边形坐标集合 
    *  @param {[test1,test2]} testPoint 测试点坐标 
    *  @return {boolean} 返回true为真，false为假 
    */  
    insidePolygon(points, testPoint){  
        var x = testPoint[0], y = testPoint[1];  
        var inside = false;  
        for (var i = 0, j = points.length - 1; i < points.length; j = i++) {  
            var xi = points[i][0], yi = points[i][1];  
            var xj = points[j][0], yj = points[j][1];  

            var intersect = ((yi > y) != (yj > y))  
                    && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);  
            if (intersect) inside = !inside;  
        }  
        return inside;  
    },
    /*
    * 	屏幕坐标与指定平面相交的three坐标
    *	鼠标的点在三维中是射线，需要借助绘制平面来转三维世界坐标
    *  @param screenX 屏幕坐标X screenY 屏幕坐标Y
    * 	@return  {THREE.Vector3}	鼠标和绘制平面相交的坐标
    */
    convertTo3DCoordinate(screenX,screenY){
        if(!drawingPlane || typeof(drawingPlane)==="undefined") return;
        var mv = new THREE.Vector3(
            (screenX / container.clientWidth) * 2 - 1,
            -(screenY / container.clientHeight) * 2 + 1,
            0.5 );
        mv.unproject(this.camera);
        var raycaster = new THREE.Raycaster(camera.position, mv.sub(camera.position).normalize());
        var intersects = raycaster.intersectObjects([drawingPlane]);	//scene.children
        if (intersects.length > 0) {
            var selected = intersects[0];//取第一个物体
            return selected.point;		//相交点坐标
        }
    },
    /*
    * 三维坐标转屏幕坐标
    * @param {Array} position 三维世界坐标[0,0,0]
    * @return {object}	屏幕坐标
    */
    threeToScreenPos(position){
        var worldVector = new THREE.Vector3(
                position[0],
                position[1],
                position[2]
        );
        var standardVector = worldVector.project(camera);//世界坐标转标准设备坐标
        var a = container.clientWidth / 2;
        var b = container.clientHeight / 2;
        var x = Math.round(standardVector.x * a + a);//标准设备坐标转屏幕坐标
        var y = Math.round(-standardVector.y * b + b);//标准设备坐标转屏幕坐标
        return {
            x: x,
            y: y
        };
    },
    //客户告警动画
    updateCustomerAlarm(){
        if(!customerAlarmArray || customerAlarmArray.length == 0) return;
        customerAlarmParam ++;
        customerAlarmParam = customerAlarmParam%2;
        if(customerAlarmParam == 0){
            for(var i=0;i<customerAlarmArray.length;i++){
                var customerAlarm = customerAlarmArray[i];
                this.changeSpriteByName(customerAlarm.name,customerAlarm.imgurl);	//正常样式
            }
        }else{
            for(var j=0;j<customerAlarmArray.length;j++){
                var customerAlarm1 = customerAlarmArray[j];
                this.changeSpriteByName(customerAlarm1.name,customerAlarm1.imgurlalarm);	//告警样式
            }
        }
    },
    /*
    *	单个客户告警 
    *  @param {String} name 模拟的客户编码 customer:10000
    *  @param {boolean} flag 告警标记(true告警 false消除告警)
    */
    customerAlarm(name,flag){
        var imgurl="";
        var imgurlalarm="";
        imgurl = DEVIMGURL.defaultP;
        imgurlalarm = DEVIMGURL.defaultPAlarm;
        
        if(!flag){
            for(var i=0;i<customerAlarmArray.length;i++){
                if(name == customerAlarmArray[i].name){
                    this.changeSpriteByName(name,imgurl);	//恢复正常
                    customerAlarmArray.splice(i,1);
                }
            }						
        }
        else{
            var isAlarm = false;
            for(var j=0;j<customerAlarmArray.length;j++){
                if(name == customerAlarmArray[j].name){
                    isAlarm = true;
                }
            }
            if(!isAlarm){
                var alarm = {name:name,imgurl:imgurl,imgurlalarm:imgurlalarm};
                customerAlarmArray.push(alarm);
            }
        }
    },
    /*
    *	设置告警动画
    *  @param {Array} customerList 客户集合
    */
    setAlarmFlash(customerList){
        var imgurl = DEVIMGURL.defaultP;
        var imgurlalarm = DEVIMGURL.defaultPAlarm;
        for(var i = 0; i < customerList.length; i++){
            var name = "customer:" + customerList[i].customernum;
            var isAlarm = false;
            for(var j = 0;j < customerAlarmArray.length; j++){
                if(name == customerAlarmArray[j].name){
                    isAlarm = true;
                    break;
                }
            }
            if(!isAlarm){
                var alarm = {name:name,imgurl:imgurl,imgurlalarm:imgurlalarm};
                customerAlarmArray.push(alarm);
            }
        }
        //去掉结束的告警动画
        for(var m = 0;m < customerAlarmArray.length; m++){
            var isAlarm = false;
            for (var n = 0; n < customerList.length; n++){
                var name = "customer:" + customerList[n].customernum;
                if(name == customerAlarmArray[m].name){
                    isAlarm = true;
                    break;
                }
            }
            if(!isAlarm){
                this.changeSpriteByName(customerAlarmArray[m].name,imgurl);	//恢复正常
                customerAlarmArray.splice(m,1);
            }
        }
    },
    /*
    *	更改客户图标
    *  @param {String} name 客户编码 customer:10000
    *  @param {String} url 图标的路径
    */
    changeSpriteByName(name,url){
        var loader = new THREE.TextureLoader();
        var texture = loader.load(url, function () { }, undefined, function () { });
        var spriteMaterial = new THREE.SpriteMaterial({ map: texture,color:0xffffff });
        
        if(customerGroup){
            var sprite = customerGroup.getObjectByName(name);   //根据名称获取
            if(sprite){
                sprite.material = spriteMaterial;
            }
        }
    },
    //渲染三维模型
    render() {
        renderer.render(scene, camera); //进行渲染
    },
    /*
    *	获取鼠标选中的客户
    *	@param {number} x 屏幕坐标X
    *	@param {number} y 屏幕坐标Y
    *	@return {THREE.Sprite || undefined} intersects 客户的Sprite
    */
    getCustomerIntersects(x,y){
        var vector = new THREE.Vector3();//三维坐标对象
        vector.set(
                ( x / container.clientWidth ) * 2 - 1,
                - ( y / container.clientHeight ) * 2 + 1,
                0.5 );
        vector.unproject(camera);
        var raycaster = new THREE.Raycaster(camera.position, vector.sub(camera.position).normalize());
        raycaster.camera = camera;
        var intersects = raycaster.intersectObjects([customerGroup],true);
        return intersects;
    },
    /*
    *   鼠标平移事件
    *   */
    onDocumentMouseMove(event) {
        var intersects = this.getCustomerIntersects(event.offsetX,event.offsetY);
        if (intersects.length > 0) {
            var selected = intersects[0];	//取第一个物体
            if(selected.object instanceof THREE.Sprite){
                $(container).css('cursor','pointer');
            }else{
                $(container).css('cursor','default');
            } 
        }else{
            $(container).css('cursor','default');
        }
    },

    /*
    *   鼠标单击事件
    *   */
    onDocumentMouseDown( event ) {
        var intersects = this.getCustomerIntersects(event.offsetX,event.offsetY);
        if (intersects.length > 0) {
            var selected = intersects[0];//取第一个物体
            if(selected.object instanceof THREE.Sprite){
                var sprite = selected.object;
                var contentStr = this.setPopContent(sprite);
        		var position = [sprite.position.x,sprite.position.y,sprite.position.z];		//弹出的经纬度
        	 	var title = "客户信息";
        	 	this.showPopup(position,title,contentStr);
            }
        }else{
            this.hidePopup();
        }
        
        var vector = new THREE.Vector3();//三维坐标对象
        vector.set(
                ( event.offsetX / container.clientWidth ) * 2 - 1,
                - ( event.offsetY / container.clientHeight ) * 2 + 1,
                0.5 );
        vector.unproject( camera );
        var raycaster = new THREE.Raycaster(camera.position, vector.sub(camera.position).normalize());
        var intersects = raycaster.intersectObjects([floorGroup.children[0]],true);
        if(intersects.length != 0){
            //highMesh = intersects[0].object;
            //highMesh.material = new THREE.MeshBasicMaterial({ color:0xff0000 });
            var point = intersects[0].point;
        }
    },
    /*
    *  弹出popup
    *  @param {Array} position [x,y,z]弹出的三维世界坐标
    *  @param {String} header 弹出框标题
    *	@param {String} content 弹出框内容
    */
    showPopup(position,title,content){
        var screenPos = this.threeToScreenPos(position);
        $("#popup-title").html(title);
        $("#popup-content").html(content);
        popPosition = position;
        var top = container.offsetTop !=0?container.offsetTop:0;
        var left = container.offsetLeft != 0?container.offsetLeft:0;
        $("#popup").css("top",screenPos.y + top - $("#popup").height()-80+"px");
        $("#popup").css("left",screenPos.x + left - $("#popup").width()/2+"px");
        $("#popup").show();
    },
    //隐藏popup弹出框
    hidePopup(){
        popPosition = undefined;
        $("#popup").hide();
    },
    //更新弹出框的位置，用户交互改变时触发
    updatePopPosition(){
        if($("#popup").css("display") != "none"){
            if(popPosition){
                var screenPos = this.threeToScreenPos(popPosition);
                var top = container.offsetTop !=0?container.offsetTop:0;
                var left = container.offsetLeft != 0?container.offsetLeft:0;
                $("#popup").css("top",screenPos.y + top - $("#popup").height()-80+"px");
                $("#popup").css("left",screenPos.x + left - $("#popup").width()/2+"px");
            }
        }
    },
    //设置popup框内容
    setPopContent(sprite){
        var data = sprite.userData;
        var contentStr = "<input type='hidden' id='customerPop' value='" + sprite.name + "'/><table class='popupTable' style='table-layout:fixed;WORD-BREAK: break-all;WORD-WRAP:break-word;border-collapse: collapse;width:100%;'><tbody>";
        contentStr += "<tr><td width='70px' align='right'>客户名称：</td>"
                + "<td align='left'><font color='#8a4141'>" + data.customerName + "</font></td></tr>"
            + "<tr><td width='70px' align='right'>客户编号：</td>"
                + "<td align='left'><font color='#8a4141'>" + data.customerNum + "</font></td></tr>"
            + "<tr><td width='70px' align='right'>手机号码：</td>"
                + "<td align='left'><font color='#8a4141'>" + (data.mobile ? data.mobile : "") + "</font></td></tr>";
        contentStr +="</tbody></table>";
        return contentStr;
    }
}

 /*
  * 创建设备弹出框标注
  *	@param {Array || undefined} options.position 三维坐标中的位置比如[0,0,0]
  *	@param {DOM || undefined} options.target 放置dom的位置
  *	@param {string || undefined} options.etype 设备类型
  *	@param {string || undefined} options.text 标注名称
  * */
 var ws3doverlay = function(options){
	var position = options.position ? options.position : [0,0,0];
	this.screenPosition = this.threeToScreenPos(position,camera);
	this.target_ = options.target ? options.target : document.body;
	this.equipmentType_ = options.etype ? options.etype : "";
	var text = options.text ? options.text:"";
			
	this.div_ = document.createElement("div");
	this.div_.innerHTML = "<span class=\"customerlabel\">"+text+"</span>";
	this.div_.className = "labeltip";
	this.target_.appendChild(this.div_);
	
	this.width = this.div_.offsetWidth;	//offsetWidth offsetHeight
	this.height = this.div_.offsetHeight;	//offsetWidth offsetHeight
	this.div_.style.top = this.screenPosition.y - 2*this.height - 10 + "px";
	this.div_.style.left = this.screenPosition.x - this.width/2 + "px";		
	this.position_ = position;
}

//获取弹出框标注的三维世界坐标
ws3doverlay.prototype.threeToScreenPos = function(position){
	var worldVector = new THREE.Vector3(
        position[0],
        position[1],
        position[2]
    );
    var standardVector = worldVector.project(camera);//世界坐标转标准设备坐标
    var a = container.clientWidth / 2;
    var b = container.clientHeight / 2;
    var x = Math.round(standardVector.x * a + a);//标准设备坐标转屏幕坐标
    var y = Math.round(-standardVector.y * b + b);//标准设备坐标转屏幕坐标
    return {
        x: x,
        y: y
    };
} 

//获取弹出框标注的三维世界坐标
ws3doverlay.prototype.getThreePosition = function(){
	return new THREE.Vector3(this.position_.x,this.position_.y,this.position_.z);
} 

//更新弹出框位置
ws3doverlay.prototype.updatePosition = function(){
	var position = this.position_ ? this.position_ : {x:0,y:0};
	
	this.screenPosition = this.threeToScreenPos(position,camera);
	
	this.div_.style.top = this.screenPosition.y - 2*this.height - 10 + "px";
	this.div_.style.left = this.screenPosition.x - this.width/2 + "px";
}

//显示
ws3doverlay.prototype.show = function(){
	this.div_.style.display = "block";
}

//获取可见性
ws3doverlay.prototype.getVisible = function(){
	var visible = true;
	if(this.div_.style.display == "none"){
		visible = false;
	}
	return visible;
}

//获取标注的设备类型，用于实时监控过滤
ws3doverlay.prototype.getEType = function(){			
	return this.equipmentType_;
}

//隐藏
ws3doverlay.prototype.hide = function(){
	this.div_.style.display = "none";
}

//获取屏幕位置坐标
ws3doverlay.prototype.getScreenPosition = function(){
	return {
		x : this.screenPosition.x,
		y : this.screenPosition.y - 2*this.height - 10
	}
}

//获取弹出坐标的尺寸，为了进行相交判断
ws3doverlay.prototype.getSize = function(){			//获取尺寸
	return{
		width:this.width,
		height:this.height
	}
}

//清除弹出标注
ws3doverlay.prototype.clear = function(){
	var div = this.div_ ? this.div_ : null;
	this.target_.removeChild(div);
}
