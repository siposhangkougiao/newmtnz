<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<title>${pd.NAME}</title>

<style>
#full-width-slider {
	width: 100%;
	color: #000;
}
.coloredBlock {
	padding: 12px;
	background: rgba(255,0,0,0.6);
	color: #FFF;
	width: 200px;
	left: 20%;
	top: 5%;
}
.infoBlock {
	position: absolute;
	top: 30px;
	right: 30px;
	left: auto;
	max-width: 25%;
	padding-bottom: 0;
	background: #FFF;
	background: rgba(255, 255, 255, 0.8);
	overflow: hidden;
	padding: 20px;
}
.infoBlockLeftBlack {
	color: #FFF;
	background: #000;
	background: rgba(0,0,0,0.75);
	left: 30px;
	right: auto;
}
.infoBlock h4 {
	font-size: 20px;
	line-height: 1.2;
	margin: 0;
	padding-bottom: 3px;
}
.infoBlock p {
	font-size: 14px;
	margin: 4px 0 0;
}
.infoBlock a {
	color: #FFF;
	text-decoration: underline;
}
.fullWidth {
	max-width: 1400px;
	margin: 0 auto 24px;
}
img{ width:100%}
 @media screen and (min-width:960px) and (min-height:660px) {
	.heroSlider .rsOverflow,  .royalSlider.heroSlider {
		height: 400px !important;
	}
}
 @media screen and (min-width:0px) and (min-height:900px) {
	.heroSlider .rsOverflow,  .royalSlider.heroSlider {
		height: 400px !important;
	}
}
@media screen and (min-width: 0px) and (max-width: 720px) {
	.royalSlider.heroSlider,  .royalSlider.heroSlider .rsOverflow {
		height: 400px !important;
	}
	.infoBlock {
		padding: 10px;
		height: auto;
		max-height: 100%;
		min-width: 40%;
		left: 5px;
		top: 5px;
		right: auto;
		font-size: 12px;
	}
	.infoBlock h3 {
		font-size: 14px;
		line-height: 17px;
	}
}
</style>

</head>
<body style="align: center;">
	<table style="width: 100%;">
		<tr><td>${pd.DETAILS}<br/><br/></td></tr>
	</table>
</body>
</html>