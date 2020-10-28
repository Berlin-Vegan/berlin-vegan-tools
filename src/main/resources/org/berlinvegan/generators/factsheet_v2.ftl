<#ftl encoding="ISO-8859-1">
<#-- @ftlvariable name="branches" type="org.berlinvegan.generators.model.GastroLocation[]" -->
<#-- @ftlvariable name="language" type="java.lang.String" -->
<style>
    ul.a {list-style-type: disc;padding-left: 30px}
</style>
<#assign branch1 = branches[0]>

<h2 class='address'>Adresse</h2>
<#list branches as branch>
<p>${branch.street}<br/>${branch.cityCode?c} ${branch.district}
    <#if branch.publicTransport??>
        <br/>${branch.publicTransport}
    </#if>
</p>
    <#if branch.telephone??>
    <p>Tel.: ${branch.telephone}</p>
    </#if>

</#list>
<#if branch1.website??>
<h2 class="internet">Internet</h2>
<p><a target='_blank' href='${branch1.website}'>${branch1.website}</a></p>
</#if>

<h2 class="openingTime">÷ffnungszeiten</h2>
${branch1.getOpenTimesHTML("de")}
<#if branch1.openComment??>
<p>${branch1.openComment}</p>
</#if>
<#if branch1.vegan == 5 || branch1.dog != -1 || branch1.organic == 1 || branch1.glutenFree == 1 || branch1.handicappedAccessible != -1
|| branch1.childChair != -1 || branch1.catering == 1 || branch1.delivery == 1
|| branch1.seatsIndoor != -1 || branch1.seatsOutdoor != -1 || branch1.wlan == 1>
<h2 class="misc">Sonstiges</h2>
<ul class="a">
    <li>${branch1.getVeganHTML("de")}</li>
    <#if branch1.organic == 1>
        <li> Bio</li>
    </#if>
    <#if branch1.glutenFree == 1>
        <li> glutenfreie Speisen</li>
    </#if>

    <#if branch1.handicappedAccessible != -1>
        <#if branch1.handicappedAccessible == 1>
            <li> Rollstuhl geeignet</li>
            <#if branch1.handicappedAccessibleWc == 1>
                <li> WC Rollstuhl geeignet</li>
            <#else>
                <li> WC Rollstuhl ungeeignet</li>
            </#if>
        <#else>
            <li> Rollstuhl ungeeignet</li>
        </#if>

    </#if>

    <#if branch1.dog != -1>
        <#if branch1.dog == 1>
            <li> Hunde erlaubt</li>
        <#else>
            <li> Hunde nicht erlaubt</li>
        </#if>
    </#if>
    <#if branch1.childChair != -1>
        <#if branch1.childChair == 1>
            <li> Kindersitz vorhanden</li>
        <#else>
            <li> kein Kindersitz vorhanden</li>
        </#if>
    </#if>
    <#if branch1.wlan == 1>
        <li> WLAN vorhanden</li>
    </#if>
    <#if branch1.catering == 1>
        <li> Catering</li>
    </#if>
    <#if branch1.delivery == 1>
        <li> Lieferservice</li>
    </#if>
    <#if branch1.seatsIndoor != -1>
        <li> Sitzpl‰tze innen: ${branch1.seatsIndoor}</li>
    </#if>
    <#if branch1.seatsOutdoor != -1>
        <li> Sitzpl‰tze auﬂen: ${branch1.seatsOutdoor}</li>
    </#if>
    <#if branch1.seatsOutdoor != -1>
    </#if>
</ul>
</#if>


