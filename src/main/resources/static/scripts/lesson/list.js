$(function () {
    $('#searchBtn').click(function(){
        pageaction();
    });
    $('#addUserInf').click(function(){
        create();
    });
    //初始化分页
    pageaction();
    var pg = $('.pagination');
    $('#pageSelect').live("change",function(){
        pg.trigger('setPage', [$(this).val()-1]);
    });
});

//分页的参数设置
var getOpt = function(){
    var opt = {
        items_per_page: 10,	//每页记录数
        num_display_entries: 3, //中间显示的页数个数 默认为10
        current_page:0,	//当前页
        num_edge_entries:1, //头尾显示的页数个数 默认为0
        link_to:"javascript:void(0)",
        prev_text:"上页",
        next_text:"下页",
        load_first_page:true,
        show_total_info:true ,
        show_first_last:true,
        first_text:"首页",
        last_text:"尾页",
        hasSelect:false,
        callback: pageselectCallback //回调函数
    }
    return opt;
}
//分页开始
var currentPageData = null ;
var pageaction = function(){
    $.get('./list?t='+new Date().getTime(),{
        name:$("#name").val(),createdateStart:$("#createdateStart").val(),createdateEnd:$("#createdateEnd").val()
    },function(data){
        currentPageData = data.content;
        $(".pagination").pagination(data.totalElements, getOpt());
    });
}

var pageselectCallback = function(page_index, jq, size){
    var html = "" ;
    if(currentPageData!=null){
        fillData(currentPageData);
        currentPageData = null;
    }else
        $.get('./list?t='+new Date().getTime(),{
            size:size,page:page_index,name:$("#name").val(),createdateStart:$("#createdateStart").val(),createdateEnd:$("#createdateEnd").val()
        },function(data){
            fillData(data.content);
        });
}
//填充分页数据
function fillData(data){
    var subrole = $("#subrole").val();
    var unsubrole = $("#unsubrole").val();

    var $list = $('#tbodyContent').empty();
    $.each(data,function(k,v){
        var html = "" ;
        html += '<tr> ' +
            '<td>'+ (v[0]==null?'':v[0]) +'</td>' +
            '<td>'+ (v[1]==null?'':v[1]) +'</td>' +
            '<td>'+ (v[1]==null?'':v[3]) +'</td>' +
            '<td>'+ (v[2]==null?'':v[2]) +'</td>';
        html += '<td><a class="c-50a73f mlr-6" href="javascript:void(0)" onclick="showDetail(\''+ v[0]+'\')">查看</a>';

        if (subrole == 'false')
            html +='<a class="c-50a73f mlr-6" href="javascript:void(0)" onclick="edit(\''+ v[0]+'\')">修改</a>';

        if (subrole == 'false')
            html += '<a class="c-50a73f mlr-6" href="javascript:void(0)" onclick="del(\''+ v[0]+'\')">删除</a>';

        if (subrole == 'true')
            html += '<a class="c-50a73f mlr-6" href="javascript:void(0)" onclick="subscribe(\''+ v[0]+'\')">订阅</a>';

        if (unsubrole == 'true')
            html +=  '<a class="c-50a73f mlr-6" href="javascript:void(0)" onclick="unsubscribe(\''+ v[0]+'\')">取消订阅</a>';

        html +='</td></tr>' ;

        $list.append($(html));
    });
}
//分页结束
var artdialog ;
function showDetail(id){
    $.get("./"+id,{ts:new Date().getTime()},function(data){
        art.dialog({
            lock:true,
            opacity:0.3,
            title: "查看信息",
            width:'750px',
            height: 'auto',
            left: '50%',
            top: '50%',
            content:data,
            esc: true,
            init: function(){
                artdialog = this;
            },
            close: function(){
                artdialog = null;
            }
        });
    });
}
function edit(id){
    $.get("./edit/"+id,{ts:new Date().getTime()},function(data){
        art.dialog({
            lock:true,
            opacity:0.3,
            title: "修改",
            width:'750px',
            height: 'auto',
            left: '50%',
            top: '50%',
            content:data,
            esc: true,
            init: function(){
                artdialog = this;
            },
            close: function(){
                artdialog = null;
            }
        });
    });
}

function subscribe(id){
    $.get("./subscribe/"+id,{ts:new Date().getTime()},function(data){
        if(data==1){
            alert("订阅成功");
            pageaction();
        }else{
            alert(data);
        }
    });
}

function unsubscribe(id){
    $.get("./unsubscribe/"+id,{ts:new Date().getTime()},function(data){
        if(data==1){
            alert("取消订阅成功");
            pageaction();
        }else{
            alert(data);
        }
    });
}
function del(id){
    if(!confirm("您确定删除此记录吗？")){
        return false;
    }
    $.get("./delete/"+id,{ts:new Date().getTime()},function(data){
        if(data==1){
            alert("删除成功");
            pageaction();
        }else{
            alert(data);
        }
    });
}
function create(){
    $.get("./new",function(data){
        art.dialog({
            lock:true,
            opacity:0.3,
            title: "新增",
            width:'750px',
            height: 'auto',
            left: '50%',
            top: '50%',
            content:data,
            esc: true,
            init: function(){
                artdialog = this;
            },
            close: function(){
                artdialog = null;
            }
        });
    });
}

function closeDialog() {
    artdialog.close();
}
