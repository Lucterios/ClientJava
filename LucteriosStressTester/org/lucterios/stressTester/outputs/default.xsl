<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
	<html>
	<head>
		<title>Stress test lucterios</title>
		<style type="text/css">
		</style>
		<META HTTP-EQUIV="refresh" CONTENT="5"/>
	</head>
<body>
	<h1>Stress test lucterios</h1>
	<h2>Statistique</h2>
  	<xsl:apply-templates/>
</body>
</html>
</xsl:template>

<xsl:template match="/test_result">
	Nombre total de test: <xsl:value-of select="count(test)"/>
	<br/>
	<br/>
	<xsl:for-each select="test">
		<xsl:sort select="@id"/>
		<xsl:if test="not(@id=preceding::*/@id)">
			<xsl:call-template name="TestStat">
				<xsl:with-param name="ID" select="@id" />
			</xsl:call-template>			
			<br/>
  		</xsl:if>
	</xsl:for-each>

</xsl:template>

<xsl:template name="TestStat">
	<xsl:param name="ID" />
	<xsl:variable name="nb_test" select="count(/test_result/test[@id=$ID])"/>
	<xsl:variable name="sum_time" select="sum(/test_result/test[@id=$ID]/@time)"/>
	<xsl:variable name="max_time">
		<xsl:for-each select="/test_result/test[@id=$ID]/@time">
			<xsl:sort data-type="number" order="descending" />
			<xsl:if test="position() = 1">
				<xsl:value-of select="number(.)" />
			</xsl:if>
		</xsl:for-each>
	</xsl:variable>
	
	<xsl:variable name="min_time">
		<xsl:for-each select="/test_result/test[@id=$ID]/@time">
			<xsl:sort data-type="number" order="ascending" />
			<xsl:if test="position() = 1">
				<xsl:value-of select="number(.)" />
			</xsl:if>
		</xsl:for-each>
	</xsl:variable>

	<table>
		<tr>
			<th colspan='2'><xsl:value-of select="/test_result/test[@id=$ID]/text()"/></th>
		</tr>
		<tr>
			<td>Nombre</td>
			<td><xsl:value-of select="$nb_test"/></td>
		</tr>
		<tr>
			<td>Temps moyen</td>
			<td><xsl:value-of select="($sum_time div $nb_test) div 100"/> secondes</td>
		</tr>
		<tr>
			<td>Temps min</td>
			<td><xsl:value-of select="($min_time) div 100"/> secondes</td>
		</tr>
		<tr>
			<td>Temps max</td>
			<td><xsl:value-of select="($max_time) div 100"/> secondes</td>
		</tr>
	</table>
</xsl:template>

</xsl:stylesheet>
