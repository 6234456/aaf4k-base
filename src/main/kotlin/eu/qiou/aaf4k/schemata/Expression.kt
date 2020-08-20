package eu.qiou.aaf4k.schemata


class Expression(val operator: Operator, val left: Value, val right: Value) :
    Value(-1, operator.calculate(left, right), "", null)
