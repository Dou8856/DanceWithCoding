from random import randint

class Die(object):
    def __init__(self, numFaces):
        if numFaces < 4:
            self.numFaces = 4
        else:
            self.numFaces = numFaces
        self.faceValue = 1
        
        
    def roll(self):
        self.faceValue = randint(1, self.numFaces)
        return self.faceValue

    def __str__(self):
        return "[%d ! %d]"% (self.faceValue, self.numFaces) # "%" is for formatted print

        
