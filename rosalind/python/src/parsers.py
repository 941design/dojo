class FastaParser(object):
    
    open_symbols = '>'
    
    def __init__(self, name):
        self.name = name
        self.buffer = None

    def parseTuples(self):
        """ Returns list of tuples
        """
        fn_add = lambda c, (k,v) : c.append((k,v),)
        return self._parse([], fn_add)

    def parseDict(self):
        """ Returns dictionary
        """
        fn_add = lambda d, (k,v) : d.update(k, v)
        return self._parse({}, fn_add)

    def _parse(self, coll, fn_add):
        self.file = open(self.name, 'r')
        self._parseInto(coll, fn_add)
        self.file.close()
        return coll

    def _parseInto(self, coll, fn_add):
        tag = self._parseTag()
        # sucks, while does not take expression as condition!
        while tag:
            s = self._parseString()
            fn_add(coll, (tag, s))
            tag = self._parseTag()
        
    def _parseTag(self):
        if self.buffer:
            tag = self.buffer
        else:
            tag = self.file.readline()
        if not tag:
            return None
        elif tag[0] in self.open_symbols:
            return tag[1:-1]
        else: 
            self._error('malformatted string')

    def _parseString(self):
        s = ''
        self.buffer = self.file.readline()
        while self.buffer \
                and not self.buffer[0] in self.open_symbols:
            s += self.buffer[:-1]
            self.buffer = self.file.readline()
        return s

    def _errror(self, comment):
        self.file.close()
        raise SyntaxError, comment
