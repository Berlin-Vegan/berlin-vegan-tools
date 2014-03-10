<#ftl encoding="ISO-8859-1">
<#-- @ftlvariable name="reviewbase" type="java.lang.String" -->
<#-- @ftlvariable name="veganStar" type="java.lang.String" -->
<#-- @ftlvariable name="restaurants" type="org.berlinvegan.generators.Restaurant[]" -->
<#-- @ftlvariable name="restaurant" type="org.berlinvegan.generators.Restaurant" -->

<#macro listPart starvalue>
<ul>
    <#list restaurants as restaurant>
        <#if restaurant.vegan == starvalue>
            <li><a href="${reviewbase}${restaurant.reviewURL}">${restaurant.name}</a>
                - <#list restaurant.districts as district>
                ${district}<#if district_has_next>,</#if>
                </#list>
            </li>
        </#if>
    </#list>
</ul>
</#macro>
<#macro renderDistrict districtName>
<h2> ${districtName} </h2>
<ul>
    <#list restaurants as restaurant>
        <#if restaurant.districts?seq_contains(districtName)>
            <li><a href="${reviewbase}${restaurant.reviewURL}">${restaurant.name}</a>
                <#list 1..restaurant.vegan as i>
                ${veganStar}
                </#list>
            </li>
        </#if>
    </#list>
</ul>
</#macro>


<script src="http://www.berlin-vegan.de/fileadmin/vorlagen/v2/jquery.min.js" type="text/javascript"></script>

<#assign veganStar = '<img src="http://www.berlin-vegan.de/fileadmin/vorlagen/images/stern.gif">'>

<div id="vegansort">
    <h2>${veganStar}${veganStar}${veganStar}${veganStar}${veganStar} 100% vegan</h2>
<@listPart starvalue=5 />
    <h2>${veganStar}${veganStar}${veganStar}${veganStar} vegetarisch-vegan, 'vegan' deklariert</h2>
<@listPart starvalue=4 />
    <h2>${veganStar}${veganStar}${veganStar} vegetarisch-vegan, 'vegan' nicht deklariert</h2>
<@listPart starvalue=3 />
    <h2>${veganStar}${veganStar} omnivor mit veganem Angebot, 'vegan' deklariert</h2>
<@listPart starvalue=2 />
    <h2>${veganStar} omnivor mit veganem Angebot, 'vegan' nicht deklariert</h2>
<@listPart starvalue=1 />
</div><div id="geosort" style="display:none">
<@renderDistrict districtName="Charlottenburg"/>
    <@renderDistrict districtName="Friedrichshain"/>
    <@renderDistrict districtName="Kreuzberg"/>
    <@renderDistrict districtName="Lichtenberg"/>
    <@renderDistrict districtName="Mitte"/>
    <@renderDistrict districtName="Neuk�lln"/>
    <@renderDistrict districtName="Prenzlauer Berg"/>
    <@renderDistrict districtName="Sch�neberg"/>
    <@renderDistrict districtName="Steglitz"/>
    <@renderDistrict districtName="Wilmersdorf"/>
    <@renderDistrict districtName="Wedding"/>
    <@renderDistrict districtName="Zehlendorf"/>


