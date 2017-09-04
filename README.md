# project-4-cgraphics
Q1	(6%) As	a	computer	graphics	expert,	you	are	approached	by	an	architect	with	a	small	contract	to	
draw	a	spiral	staircase	that	is	customizable	with	different	parameters.	You	then	notice	that	the	
requested	spiral	staircase	looks	very	similar	to	that	in	Exercise	6.10	in	Page	217	of	the	textbook.		Here	
are	the	architect's	requirements:	Java	program	file	is	named	as	Stairs.java.	The	program	should	be	able	
to	produce	a	data	file	that	can	be	viewed	using	Painter.java	and	ZBuff.java	in	Chapter	7	or	HLines.java	in	
Chapter	6	and	look	VERY	similar	to	the	one	in	the	book.	There	should	be	no	gap	between	the	stairs	and	
the	central	cylinder.	A	sample	data	file	called	stairs.dat	may	be	downloaded	from	eLearning	for	you	to	
view	it	in	action.	You	can	run	the	compiled	Painter.java,	ZBuff.java,	or	HLines.java	and	then	load	
stairs.dat.
The	Stairs.java	program	should	take	3	command-line	arguments	for	the	three	customizable	parameters.	
In	other	words,	after	compiling	your	program,	you	may	run	it	by	
java	Stair	25	15	stairs.dat
where	25	is	the	number	of	stairs,	15	is	the	rotation	degree	between	every	two	consecutive	stairs,	and	
stairs.dat	is	the	output	data	file.	


Q2	(4%)	Modify	FractalGrammars.java	program	provided	in	the	textbook,	 so	that	when	the	curve	turns	
90	degree,	it	will	draw	a	rounded corner,	like	the	Dragon	curve	in	Figure	8.4.	Another	example that	you	
could	test	is	a	variation	of	Koch	curve,	with	the	grammar	defined	as:
(F-F-F-F,	FF-F-F-F-F-F+F,	nil,	nil,	nil,	90)
The	second	generation	image	showing	rounded	corners	can be found	in	the	attached	file	
"RoundedCorners.png".
