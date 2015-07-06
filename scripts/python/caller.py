from point import *
from line import *
from die import *
from studentRecord import *

studentA = Student()
print studentA

studentB = Student("200609020130",0, "Emily", "He", 4.5)
print studentB
print studentB.getClass()

studentC = Student("2006090201302", 1, "Xuemimg", "He", 4.6)

newClass = Class()
newClass.addStudent(studentB)
newClass.addStudent(studentC)
print newClass
