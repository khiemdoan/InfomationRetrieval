from HTMLParser import HTMLParser
import xml.etree.ElementTree as ET
h = HTMLParser()

def to_files(xml_path):
	tree = ET.parse(xml_path)
	root = tree.getroot()
	to_file(root[0],0)
	to_file(root[1],len(root[0]))
	to_file(root[2],len(root[0]) + len(root[1]))
	to_file(root[3],len(root[0]) + len(root[1])+ len(root[2]))

def to_file(element, offset):
        for i in range(len(element)):
		id = i + offset
                filename = "/home/thinhnt/NetBeansProjects/IR/data/data/" + str(id) + "." + element.attrib['name']
                f = open(filename,"w")
                title = element[i].attrib['title'].strip()
                title = h.unescape(title) + "\n"
                f.write(title.encode('utf-8'))
                content = element[i].text.strip()
                content = h.unescape(content)
                f.write(content.encode('utf-8'))
                f.close()


