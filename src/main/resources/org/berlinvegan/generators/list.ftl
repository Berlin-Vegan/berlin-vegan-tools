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
Sortierung nach:
<div id="vegansortbutton" class="buttonsmall orangeactive orange buttonright"
     onclick="jQuery('#vegansort').show();jQuery('#geosort').hide();jQuery('#rankingsort').hide();jQuery('#geosortbutton').removeClass('orangeactive');jQuery('#rankingsortbutton').removeClass('orangeactive');jQuery('#vegansortbutton').addClass('orangeactive');">
    Veganfreundlich
</div>
<div id="geosortbutton" class="buttonsmall orange buttoncenter"
     onclick="jQuery('#vegansort').hide();jQuery('#rankingsort').hide();jQuery('#geosort').show();jQuery('#vegansortbutton').removeClass('orangeactive');jQuery('#rankingsortbutton').removeClass('orangeactive');jQuery('#geosortbutton').addClass('orangeactive');">
    Bezirken
</div>
<div id="rankingsortbutton" class="buttonsmall orange buttonleft"
     onclick="jQuery('#vegansort').hide();jQuery('#geosort').hide();jQuery('#rankingsort').show();jQuery('#vegansortbutton').removeClass('orangeactive');jQuery('#geosortbutton').removeClass('orangeactive');jQuery('#rankingsortbutton').addClass('orangeactive');">
    Bewertung
</div>
<link rel="stylesheet" type="text/css" href="http://www.berlin-vegan.de/fileadmin/vorlagen/v2/style.css"
      media="screen, projection" title="Standard"/>
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
</div>

<div id="geosort" style="display:none">
<@renderDistrict districtName="Charlottenburg/Wilmersdorf"/>
    <@renderDistrict districtName="Friedrichshain"/>
    <@renderDistrict districtName="Lichtenberg"/>
    <@renderDistrict districtName="Mitte/Wedding"/>
    <@renderDistrict districtName="Neukölln"/>
    <@renderDistrict districtName="Prenzlauer Berg"/>
    <@renderDistrict districtName="Steglitz/Zehlendorf"/>
    <@renderDistrict districtName="Tempelhof/Schöneberg"/>
</div>
<div id="rankingsort" style="display:none"><br/>
    <ul>
    <#list restaurants?sort_by(['rating', 'value'])?reverse as restaurant>
        <#if (restaurant.rating.number > 15) >
            <li><a href="${reviewbase}${restaurant.reviewURL}">${restaurant.name}</a>
                <#list 1..restaurant.vegan as i>
                ${veganStar}
                </#list>
                 - ${restaurant.rating.value} ( ${restaurant.rating.number} Bewertungen )
            </li>
        </#if>
    </#list>
    </ul>
</div>