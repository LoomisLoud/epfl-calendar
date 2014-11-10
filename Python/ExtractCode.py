from xlrd import open_workbook
#coding: utf8
import requests
import re
import os
import json

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
			jsonResume = jsonObj['courseBook']['paragraphs'][0]['content']

			#jsonResume = re.sub('(<br />)?(<br />)?','',jsonResume)
			#jsonResume = re.sub('(<p>)?(</p>)?','', jsonResume)
			jsonResume = re.sub('(<[a-z/ ]*>)','',jsonResume)
		except IndexError :
			print('no description for now...')
	return jsonResume
   
colCodeCourse = 0
colCourseName = 1
colCredit = -1
colEnseignant = -1
urlAppEngine = "http://versatile-hull-742.appspot.com"
#urlAppEngine = "http://localhost:8080"
regex = '(^[A-Z]+)-([0-9]+\(?[a-z]*\)?]*$)'
regexCode = '^Code[s]?$'
regexCodeMulCols = '^2ème$'
regexEnseignant = '.*(Enseignants).*'
credit = 0
twoColCredit = false
#url details
urlDetails = 'https://isa.epfl.ch/services/books/2013-2014/course/'
#for each .xls
for fn in os.listdir('.'):
	if os.path.isfile(fn):
		matchObj = re.match('.*\.xls$',fn)
		if matchObj:
			book = open_workbook(fn,on_demand=True)
			worksheets = book.sheet_names()
			for worksheet_name in worksheets:
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
								#check if we have 2 column (2ème and 3ème)
								nameColSub = worksheet.cell(x+2,y).value.encode('utf8')
								if (nameColSub == '2ème' or nameColSub == '3ème'):
									twoColCredit = true
								colCredit = y
								rowCredit = x
								#print('Found colCredit = '+str(colCredit))
							if matchObjEnseignant:
								colEnseignant = y
								#print('Found colEnseignant = '+str(colEnseignant))
					#REGEX, check if line has a code-course
					if (x == rowCredit+2) :
						if (isinstance(worksheet.cell(x, colCredit).value, unicode)) :
							matchObj = re.match(regexCodeMulCols, worksheet.cell(x, colCredit).value.encode('utf8'))
							if matchObj:
								mulColCredit = True
								print("mulColCredit : true")
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
							credit = -1

						#print('credit = ' + str(credit))
						#payload = {'code': worksheet.cell(x, colCodeCourse).value.encode('utf8'), 'name': worksheet.cell(x, colCourseName).value.encode('utf8')}
						#r = requests.post("http://localhost:8080/", data=payload)
						#payload = {'name': 'Embedded systems', 'code' : 'CS-473', 'description' : 'mock description', 'numberOfCredits' : '4', 'professorName' : 'Pr. Beuchat'}
						#json from website
						urlDetailsFinal = urlDetails + worksheet.cell(x,colCodeCourse).value.encode('utf8')
						details = getContentDetailsFromUrl(urlDetailsFinal)
						#print details.encode('utf8')
						#TODO ADD TO PAYLOAD
						payload = {'name': worksheet.cell(x,colCourseName).value.encode('utf8'), 'code' : worksheet.cell(x,colCodeCourse).value.encode('utf8'), 'description' : details.encode('utf8'),'numberOfCredits' : credit, 'professorName' : enseignant}
						r = requests.post(urlAppEngine + "/course/create", data=json.dumps(payload))
						if (r.status_code != requests.codes.ok):
							print 'Error in request to app engine, failed to post payload'
							exit(1)
				#unload sheet
				book.unload_sheet(worksheet_name)
				#reset var
				colCredit = -1
				colEnseignant = -1
				twoColCredit = false
			print('Done doing book ' + fn + ' \n')