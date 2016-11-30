from HTMLParserttrib['name']+ import HTMLParser
h = HTMLParser()
def to_file(element):
	for i in range(len(element)):
		filename = "/home/thinhnt/NetBeansProjects/IR/data/"+element.attrib['name']+"/" + str(i) + ".txt" 
		f = open(filename,"w")
		title = element[i].attrib['title'].strip()
		title = h.unescape(title) + "\n"
		f.write(title.encode('utf-8'))
		content = element[i].text.strip()
		content = h.unescape(content)
		f.write(content.encode('utf-8'))
		f.close()

