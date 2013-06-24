<#ftl encoding="ISO-8859-1">
<#-- @ftlvariable name="branches" type="org.berlinvegan.generators.Restaurant[]" -->
<#-- @ftlvariable name="language" type="java.lang.String" -->

<#assign branch1 = branches[0]>
<script type='text/javascript'>var disqus_identifier = '${branch1.reviewURL}';</script>

<h1 class='csc-firstHeader'>Adresse</h1>
<#list branches as branch>
<p>${branch.street}<br/>${branch.cityCode?c} ${branch.district}
	<#if branch.bvg??>
        <br/>${branch.bvg}
	</#if>
</p>
	<#if branch.telephone??>
    <p>Tel.: ${branch.telephone}</p>
	</#if>

</#list>
<#if branch1.website??>
<h1>Internet</h1>
<p><a target='_blank' href='http://${branch1.website}'>${branch1.website}</a></p>
</#if>

<h1>÷ffnungszeiten</h1>
${branch1.getOpenTimesHTML("de")}
<#if branch1.openComment??>
<p>${branch1.openComment}</p>
</#if>
<#if branch1.vegan == 5 || branch1.dog != -1 || branch1.organic == 1 || branch1.glutenFree == 1 || branch1.ha != -1
|| branch1.childChair != -1 || branch1.catering == 1 || branch1.delivery == 1
|| branch1.seat_in != -1 || branch1.seat_out != -1 || branch1.wlan == 1>
<h1>Sonstiges</h1>
<ul>
	<#if branch1.vegan == 5>
        <li> 100% vegan</li>
	</#if>
	<#if branch1.organic == 1>
        <li> Bio</li>
	</#if>
    <#if branch1.glutenFree == 1>
        <li> glutenfreie Speisen</li>
    </#if>

	<#if branch1.ha != -1>
		<#if branch1.ha == 1>
            <li> Rollstuhl geeignet</li>
			<#if branch1.ha_wc == 1>
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
	<#if branch1.seat_in != -1>
        <li> Sitzpl‰tze innen: ${branch1.seat_in}</li>
	</#if>
	<#if branch1.seat_out != -1>
        <li> Sitzpl‰tze auﬂen: ${branch1.seat_out}</li>
	</#if>
	<#if branch1.seat_out != -1>
	</#if>
</ul>
</#if>


<script type="text/javascript">
    var textDiv = document.getElementById('text');
    var disqusDiv = document.createElement('div');
    disqusDiv.setAttribute('id', "disqus_thread");
    textDiv.appendChild(disqusDiv);
</script>
<!--<div id="disqus_thread"></div>-->

<style type="text/css">
    #dsq-content ul li {
        background: none;
        padding-left: 0px;
    }

    #dsq-content .dsq-options {
        font-size: 10px;
    }

    #dsq-content .dsq-pagination {
        display: none;
    }

    #dsq-content #dsq-footer {
        margin: 0;
        font-size: 11px;
    }

    #dsq-comment-footer {
        font-size: 9px;
    }
</style>
<script type="text/javascript">
    /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
    var disqus_shortname = 'berlin-vegan'; // required: replace example with your forum shortname

    /* * * DON'T EDIT BELOW THIS LINE * * */
    (function () {
        var dsq = document.createElement('script');
        dsq.type = 'text/javascript';
        dsq.async = true;
        dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';
        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
    })();
</script>
<noscript>Please enable JavaScript to view the <a href="http://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>