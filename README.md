# PVN-Java

The java-lib for PVN from [LordAxi](https://github.com/LordAxi): [PVN-Repository](https://github.com/LordAxi/PandaVariableNotation).

# How to use

First create an instance of PVNInterpreter:
PVNInterpreter interpreter = new PVNInterpreter(this.getClass().getClassLoader().getResource("test.pvn"));

Use interpreter.getPvn() to get the content ob the .pvn file in a HashMap<String, Object>

Use interpreter.grabPvn(inst) to overwrite the variables in the Class-Instance:

class Test {
  public int test_int;
  public int test_str;
}
class Main {
  public Main() {
    Test test = new Test();
    PVNInterpreter interpreter = new PVNInterpreter(this.getClass().getClassLoader().getRecource("test.pvn));
    interpreter.grabPvn(test);
  }
}

overwrites (test_int) and (test_str) with the data of the test.pvn.
