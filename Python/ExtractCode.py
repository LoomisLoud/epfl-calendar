from xlrd import open_workbook
#coding: utf8
import requests
import re
import os
import json
import HTMLParser

def getContentDetailsFromUrl(url):
	headers = {'Accept': 'application/json'}
	r=requests.get(url, headers=headers)
	#check status of response
	jsonResume = ''
	if (r.status_code == requests.codes.ok):
		#decodeRes = r.text.decode('utf-8')
		data = json.loads(r.text)
		#print 'INDENT:', json.dumps(data, sort_keys=True, indent=2)
		try :
			jsonObj = data[0]
			categoryLen = len(jsonObj['courseBook']['paragraphs'])
			i = 0
			while(i < categoryLen):
				lang = jsonObj['courseBook']['paragraphs'][i]['lang']
				if (jsonObj['courseBook']['paragraphs'][i]['type']['code'] == "RUBRIQUE_RESUME" and lang == "en"):
					jsonResume = jsonObj['courseBook']['paragraphs'][i]['content']
					break
				i = i + 1
			#remove html balises
			#jsonResume = re.sub('(<br />)?(<br />)?','',jsonResume)
			#jsonResume = re.sub('(<p>)?(</p>)?','', jsonResume)
			jsonResume = re.sub('(<[a-z/ ]*>)','',jsonResume)
		except IndexError :
			print('no description for now...')
	return jsonResume
	
def cleanCourseName(courseName):
	regexCourseName = '(?P<body>.*)(?P<extraStuff>( \(.*\) ou$)|(\(.*\))?(\*\*$))'
	matchCourseName =  re.match(regexCourseName,courseName)
	if matchCourseName:
		#print 'we found a (en français) : '+ courseName
		#only take first group (we dont take " (en français)) ou"
		courseName = matchCourseName.group('body')
		#print 'coursename : ' + courseName
		
	regexCourseName = '(?P<extraStuff>^- ?)(?P<body>.*$)'
	matchCourseName = re.match(regexCourseName, courseName)
	if matchCourseName:
		#print 'we found a -smthing : ' + courseName
		courseName = matchCourseName.group('body')
		#print 'courseName : ' + courseName
	return courseName
	
   
colCodeCourse = 0
colCourseName = 1
colCredit = -1
colEnseignant = -1
urlAppEngine = "http://versatile-hull-742.appspot.com"
#urlAppEngine = "http://localhost:8080"
regex = '(^[A-Z]+)-([0-9]+\(?[a-z]*\)?]*$)'
regexCode = '^Code[s]?$'
regexCodeMulCols = '^2ème$'
regexEnseignant = '(?i)((.*(Enseignants).*)|(.*(coordinateurs).*))'
regexCredit = '^\((?P<body>[0-9]+)\)$'
credit = 0
#url details
urlDetails = 'https://isa.epfl.ch/services/books/2014-2015/course/'
#for each .xls
for fn in os.listdir('.'):
	if os.path.isfile(fn):
		matchObj = re.match('.*\.xls$',fn)
		if matchObj:
			book = open_workbook(fn,on_demand=True)
			worksheets = book.sheet_names()
			for worksheet_name in worksheets:
				#reset var
				colCredit = -1
				colEnseignant = -1
				mulColCredit = False
				rowCredit = 0
				worksheet = book.sheet_by_name(worksheet_name)
				if(worksheet.visibility == 1 or worksheet.visibility == 2) :
					continue
				print(worksheet_name)
				print('Doing sheet ' + worksheet_name + ' \n')
				for x in range(0,worksheet.nrows):
					#print('Row ' + str(x))
					#loop (usually two) until we find the title row (code-matiere-...)
					matchObj = re.match(regexCode, worksheet.cell(x, colCodeCourse).value.encode('utf8'))
					if matchObj:
						#search for the index of crédits
						#print('Searching for index of crédits')
						for y in range(0,worksheet.ncols):
							nameCol = worksheet.cell(x,y).value.encode('utf8')
							matchObjEnseignant = re.match(regexEnseignant, worksheet.cell(x,y).value.encode('utf8'))
							#print('col '+str(y)+' = '+ nameCol)
							if (nameCol == 'Crédits' or nameCol == 'Coeff.'):
								colCredit = y
								rowCredit = x
								#print('Found colCredit = '+str(colCredit))
							if matchObjEnseignant:
								colEnseignant = y
								#print('Found colEnseignant = '+str(colEnseignant))
					if (x == rowCredit+2) :
						if (isinstance(worksheet.cell(x, colCredit).value, unicode)) :
							matchObj = re.match(regexCodeMulCols, worksheet.cell(x, colCredit).value.encode('utf8'))
							if matchObj:
								mulColCredit = True
								print("mulColCredit : true")
					#REGEX, check if line has a code-course
					matchObj = re.match(regex, worksheet.cell(x, colCodeCourse).value.encode('utf8'))
					if matchObj:
						if (colCredit == -1):
							credit = -1
						else:
							credit = worksheet.cell(x,colCredit).value
						if (colEnseignant == -1):
							enseignant = ""
						else:
							enseignant = worksheet.cell(x,colEnseignant).value.encode('utf8')
						if (isinstance(credit, str) or isinstance(credit, unicode)) :
							if (mulColCredit == True) :
								credit = worksheet.cell(x,colCredit+1).value
						
						if (isinstance(credit, str) or isinstance(credit, unicode)) :
							if (credit == ''):
								credit = -1
							else :
								matchCredit = re.match(regexCredit, credit)
								if matchCredit:
									credit = int(matchCredit.group('body'))
								else:
									credit = -1
						#print('credit = ' + str(credit))
						#payload = {'code': worksheet.cell(x, colCodeCourse).value.encode('utf8'), 'name': worksheet.cell(x, colCourseName).value.encode('utf8')}
						#r = requests.post("http://localhost:8080/", data=payload)
						#payload = {'name': 'Embedded systems', 'code' : 'CS-473', 'description' : 'mock description', 'numberOfCredits' : '4', 'professorName' : 'Pr. Beuchat'}
						#json from website
						code = worksheet.cell(x,colCodeCourse).value.encode('utf8')
						code = re.sub('([a-z]+)$', r'(\1)', code)
						print(code)
						urlDetailsFinal = urlDetails + code
						details = getContentDetailsFromUrl(urlDetailsFinal)
						htmlParser = HTMLParser.HTMLParser()
						details = htmlParser.unescape(details)
						#print details.encode('utf8')
						courseName = worksheet.cell(x,colCourseName).value.encode('utf8')
						courseName = cleanCourseName(courseName)
						#debug
						if (credit == -1):
							print "Credit -1 on : " + code
							print "ColCredit : " + str(colCredit)
							with open('Log/'+code + 'logCreditError.txt', 'w') as logFile:
								logFile.write('Book : ' + fn.encode('utf8') + '\r\n' + 'Sheet : ' + worksheet_name.encode('utf8') + '\r\n' +'colCredit : ' + str(colCredit))
							#default value to store course without credit
							credit = 0
						payload = {'name': courseName.lstrip().rstrip(), 'code' : code, 'description' : details.encode('utf8'),'numberOfCredits' : credit, 'professorName' : enseignant}
						r = requests.post(urlAppEngine + "/course/create", data=json.dumps(payload))
						if (r.status_code != requests.codes.ok):
							print 'Error in request to app engine, failed to post payload'
							exit(1)
				#unload sheet
				book.unload_sheet(worksheet_name)
			print('Done doing book ' + fn + ' \n')