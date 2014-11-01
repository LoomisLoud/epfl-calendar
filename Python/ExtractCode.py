from xlrd import open_workbook
#coding: utf8
import requests
import re
import os
import json


colCodeCourse = 0
colCourseName = 1
colCredit = -1
url = "http://versatile-hull-742.appspot.com"
regex = '(^[A-Z]+)-([0-9]+\(?[a-z]*\)?]*$)'
regexCode = '^Code[s]?$'
regexEnseignant = '.*(Enseignants).*'
credit = 0
#for each .xls
for fn in os.listdir('.'):
	if os.path.isfile(fn):
		matchObj = re.match('.*\.xls$',fn)
		if matchObj:
			book = open_workbook(fn,on_demand=True)
			worksheets = book.sheet_names()
			for worksheet_name in worksheets:
				worksheet = book.sheet_by_name(worksheet_name)
				print('Doing sheet ' + worksheet_name + ' \n')
				for x in range(0,worksheet.nrows):
					#print('Row ' + str(x))
					#loop (usually two) until we find the title row (code-matiere-...)
					matchObj = re.match(regexCode, worksheet.cell(x, colCodeCourse).value.encode('utf8'))
					if matchObj:
						#search for the index of crédits
						print('Searching for index of crédits')
						for y in range(0,worksheet.ncols):
							nameCol = worksheet.cell(x,y).value.encode('utf8')
							matchObjEnseignant = re.match(regexEnseignant, worksheet.cell(x,y).value.encode('utf8'))
							#print('col '+str(y)+' = '+ nameCol)
							if (nameCol == 'Crédits' or nameCol == 'Coeff.'):
								colCredit = y
								print('Found colCredit = '+str(colCredit))
							if matchObjEnseignant:
								colEnseignant = y
								print('Found colEnseignant = '+str(colEnseignant))
					#REGEX, check if line has a code-course
					matchObj = re.match(regex, worksheet.cell(x, colCodeCourse).value.encode('utf8'))
					if matchObj:
						if (colCredit == -1):
							credit = -1
						else:
							credit = worksheet.cell(x,colCredit).value
						if (colEnseignant == -1):
							enseignant = -1
						else:
							enseignant = worksheet.cell(x,colEnseignant).value.encode('utf8')
						#print('credit = ' + str(credit))
						#payload = {'code': worksheet.cell(x, colCodeCourse).value.encode('utf8'), 'name': worksheet.cell(x, colCourseName).value.encode('utf8')}
						#r = requests.post("http://localhost:8080/", data=payload)
						#payload = {'name': 'Embedded systems', 'code' : 'CS-473', 'description' : 'mock description', 'numberOfCredits' : '4', 'professorName' : 'Pr. Beuchat'}
						payload = {'name': worksheet.cell(x,colCourseName).value.encode('utf8'), 'code' : worksheet.cell(x,colCodeCourse).value.encode('utf8'), 'description' : 'mock description','numberOfCredits' : credit, 'professorName' : enseignant}
						r = requests.post(url + "/createCourse", data=json.dumps(payload))	
				#unload sheet
				book.unload_sheet(worksheet_name)
				#reset var
				colCredit = -1
				colEnseignant = -1
			print('Done doing book ' + fn + ' \n')