class mutable_inheritance(type):
    def __new__(cls, cls_name, cls_bases, cls_dict):
        out = super().__new__(cls, cls_name, cls_bases, cls_dict)
        out.switch_parents = classmethod(cls.switch_parents)
        out.switched = False
        return out

    @staticmethod
    def switch_parents(cls):
        cls.switched = not cls.switched
        cls.__bases__  = cls.__bases__ + tuple()

    def mro(cls):
        default_mro = super().mro()
        if hasattr(cls, "switched") and cls.switched:
            b = list(default_mro)
            b[1], b[2] = b[2], b[1]
            return b
        else:
            return default_mro

class A():
    def foo(self):
        print('the parent is A')

class B():
    def foo(self):
        print('the parent is B')

class C(A, B, metaclass=mutable_inheritance):
    def foo(self):
        super().foo()

a = C()
a.foo()
C.switch_parents()
a.foo()
C.switch_parents()
a.foo()