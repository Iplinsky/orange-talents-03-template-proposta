package br.com.zupacademy.proposta.handlers;

public class CamposDeErroDto {

    private String campo;
    private String erro;

    public CamposDeErroDto(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public String getErro() {
        return erro;
    }
}
