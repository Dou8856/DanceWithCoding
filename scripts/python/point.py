class Point(object):
    def __init__(self,x = 0, y = 0):#set the default value for attributes
        self.xCoord = x
        self.yCoord = y
    def __str__(self):
        return "(xCoord = " + str(self.xCoord) + " yCoord = " + str(self.yCoord)+")"
    def getX(self):
        return self.xCoord
    def getY(self):
        return self.yCoord
    def shift(self, xShift, yShift):
        self.xCoord = self.xCoord + xShift
        self.yCoord = self.yCoord + yShift

