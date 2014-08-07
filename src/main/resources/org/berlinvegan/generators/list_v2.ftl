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
                <#if restaurant.vegan == 5>
                ${veganStar}${veganStar}${veganStar}
                </#if>
                <#if restaurant.vegan == 4>
                ${veganStar}${veganStar}
                </#if>
                <#if restaurant.vegan == 3>
                ${veganStar}${veganStar}
                </#if>
                <#if restaurant.vegan == 2>
                ${veganStar}
                </#if>
            </li>
        </#if>
    </#list>
</ul>
</#macro>


<#assign veganStar = '<img src="http://www.berlin-vegan.de/fileadmin/vorlagen/images/stern.gif">'>

<div id="vegansort">
    <h2>${veganStar}${veganStar}${veganStar} Vegan</h2>
<@listPart starvalue=5 />
    <h2>${veganStar}${veganStar} Vegetarisch (vegan deklariert)</h2>
<@listPart starvalue=4 />
    <h2>${veganStar} Omnivor (vegan deklariert)</h2>
<@listPart starvalue=2 />
</div>
<div id="geosort" style="display:none">
<@renderDistrict districtName="Charlottenburg"/>
    <@renderDistrict districtName="Friedrichshain"/>
    <@renderDistrict districtName="Köpenik"/>
    <@renderDistrict districtName="Kreuzberg"/>
    <@renderDistrict districtName="Lichtenberg"/>
    <@renderDistrict districtName="Mitte"/>
    <@renderDistrict districtName="Neukölln"/>
    <@renderDistrict districtName="Prenzlauer Berg"/>
    <@renderDistrict districtName="Schöneberg"/>
    <@renderDistrict districtName="Steglitz"/>
    <@renderDistrict districtName="Wedding"/>
    <@renderDistrict districtName="Zehlendorf"/>


