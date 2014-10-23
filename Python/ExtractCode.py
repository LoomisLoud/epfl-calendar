from xlrd import open_workbook

book = open_workbook('INplan2014-2015.xls',on_demand=True)

sheet0 = book.sheet_by_index(0)
fCodeCourse = open('codeCourse.txt','w')
fCourseName = open('courseName.txt','w')
colCodeCourse = 0
colCourseName = 1
for x in range(0,sheet0.nrows):
	fCodeCourse.write(sheet0.cell(x,colCodeCourse).value.encode('utf8'))
	fCodeCourse.write('\n')
	fCourseName.write(sheet0.cell(x,colCourseName).value.encode('utf8'))
	fCourseName.write('\n')