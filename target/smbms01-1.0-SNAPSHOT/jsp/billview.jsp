<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/jsp/common/head.jsp"%>
<div class="right">
     <div class="location">
         <strong>你现在所在的位置是:</strong>
         <span>订单管理页面 >> 信息查看</span>
     </div>
     <div class="providerView">
         <p><strong>订单编号：</strong><span>${sessionScope.bill.billCode }</span></p>
         <p><strong>商品名称：</strong><span>${sessionScope.bill.productName }</span></p>
         <p><strong>商品类别：</strong><span>${sessionScope.bill.produceDesc }</span></p>
         <p><strong>商品单位：</strong><span>${sessionScope.bill.productUnit }</span></p>
         <p><strong>商品数量：</strong><span>${sessionScope.bill.productCount }</span></p>
         <p><strong>总金额：</strong><span>${sessionScope.bill.totalPrice }</span></p>
         <p><strong>供应商：</strong><span>${sessionScope.bill.providerName }</span></p>
         <p><strong>创建时间：</strong><span>${sessionScope.bill.creationDate }</span></p>
         <p><strong>订单创建者：</strong><span>${sessionScope.bill.createdName }</span</p>
         <p><strong>是否付款：</strong>
         	<span>
         		<c:if test="${sessionScope.bill.isPayment == 1}">未付款</c:if>
				<c:if test="${sessionScope.bill.isPayment == 2}">已付款</c:if>
			</span>
		</p>
		<div class="providerAddBtn">
         	<input type="button" id="back" name="back" value="返回" >
        </div>
     </div>
 </div>
</section>
<%@include file="/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/billview.js"></script>