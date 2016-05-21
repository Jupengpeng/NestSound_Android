package com.xilu.wybz.http.rsa;

public class SecurityConfig {
    public final static String RSA_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMQgbB4+nreisjTxpn69YqBQklw3scOuBWlf2mNrbjrm3Ott1YuJ/bpGGIifaVXPpC4RaEnCUztxMxhgfVtpzLhnt7V44gfqGWnf9MqJkyGM0lvJp+5CMGS+zixBU5pb8biIq7lb5IVxh0I6JOjq9l7ooP0hsFdovU/cGRoweraRAgMBAAECgYA7sxbWGf+cLFUMkqY4nBNic0Qp9/YPd3BERB4o9zGlklKbA2LmR8nJIr8jD0s0CLGUKE5TbWiChpGkEPL3mXvr+IoZosaVi09lDEDMwnRJJxWetYWbEEDh1dB3tv7Ny0it8vvVbDIo+2FX5Q9zgLJYM41ezl+7h6WVGtY0AHQiVQJBAP1/m9fuRUlnIbkmIexjkWCaCN59juhCAHOCN7832hdExTSc0AMeSv1BFH3Sft9lyovZ7YcF4aohprcvTFLDAy8CQQDGD+FOt588hmOrYlMbNyO3+KaMMwbsPOAlifgIAt8AYveEIeYS9PMzMfn2l0R0y4jNLAzFe1Oayly4TlZ5jnI/AkEArFAFmy23o0GbRsOI46p6s3OA+9vVPENBE0M8qZpJgO+aLT06mCQLTULjrvNakngayh2Eu/dfgcoGDRb1hnxQuQJATdgj96pX3ZP8THnirAmp8j66RtQvXl42wspNP+jQ+PfszHP+V2kKxQ5Zbj/Z2gW9CNbNVji4jVgHxTCU8EW6RQJBANqcjJOPoCNoNAmMiAfRhs/HCrofZZUA9Hwa1mEqFh3Xk0sr3h5QhchSRIj4Cl4HHgt6J/w7dFOSp1OUf4MKqLU=";
    public final static String RSA_PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEIGwePp63orI08aZ+vWKgUJJcN7HDrgVpX9pja2465tzrbdWLif26RhiIn2lVz6QuEWhJwlM7cTMYYH1bacy4Z7e1eOIH6hlp3/TKiZMhjNJbyafuQjBkvs4sQVOaW/G4iKu5W+SFcYdCOiTo6vZe6KD9IbBXaL1P3BkaMHq2kQIDAQAB";
//    public final static String RSA_PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfRTdcPIH10gT9f31rQuIInLwe"
//            + "\r" + "7fl2dtEJ93gTmjE9c2H+kLVENWgECiJVQ5sonQNfwToMKdO0b3Olf4pgBKeLThra" + "\r"
//            + "z/L3nYJYlbqjHC3jTjUnZc0luumpXGsox62+PuSGBlfb8zJO6hix4GV/vhyQVCpG" + "\r"
//            + "9aYqgE7zyTRZYX9byQIDAQAB" + "\r";
//
//        public final static String RSA_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ9FN1w8gfXSBP1/"
//                + "\r" + "fWtC4gicvB7t+XZ20Qn3eBOaMT1zYf6QtUQ1aAQKIlVDmyidA1/BOgwp07Rvc6V/" + "\r"
//                + "imAEp4tOGtrP8vedgliVuqMcLeNONSdlzSW66alcayjHrb4+5IYGV9vzMk7qGLHg" + "\r"
//                + "ZX++HJBUKkb1piqATvPJNFlhf1vJAgMBAAECgYA736xhG0oL3EkN9yhx8zG/5RP/" + "\r"
//                + "WJzoQOByq7pTPCr4m/Ch30qVerJAmoKvpPumN+h1zdEBk5PHiAJkm96sG/PTndEf" + "\r"
//                + "kZrAJ2hwSBqptcABYk6ED70gRTQ1S53tyQXIOSjRBcugY/21qeswS3nMyq3xDEPK" + "\r"
//                + "XpdyKPeaTyuK86AEkQJBAM1M7p1lfzEKjNw17SDMLnca/8pBcA0EEcyvtaQpRvaL" + "\r"
//                + "n61eQQnnPdpvHamkRBcOvgCAkfwa1uboru0QdXii/gUCQQDGmkP+KJPX9JVCrbRt" + "\r"
//                + "7wKyIemyNM+J6y1ZBZ2bVCf9jacCQaSkIWnIR1S9UM+1CFE30So2CA0CfCDmQy+y" + "\r"
//                + "7A31AkB8cGFB7j+GTkrLP7SX6KtRboAU7E0q1oijdO24r3xf/Imw4Cy0AAIx4KAu" + "\r"
//                + "L29GOp1YWJYkJXCVTfyZnRxXHxSxAkEAvO0zkSv4uI8rDmtAIPQllF8+eRBT/deD" + "\r"
//                + "JBR7ga/k+wctwK/Bd4Fxp9xzeETP0l8/I+IOTagK+Dos8d8oGQUFoQJBAI4Nwpfo" + "\r"
//                + "MFaLJXGY9ok45wXrcqkJgM+SN6i8hQeujXESVHYatAIL/1DgLi+u46EFD69fw0w+" + "\r" + "c7o0HLlMsYPAzJw="
//                + "\r";
}