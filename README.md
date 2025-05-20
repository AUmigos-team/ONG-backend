# 🐾 AUamigos – Fluxo de Trabalho com Git

Este documento define o padrão de trabalho com Git adotado pela equipe do projeto AUamigos.

---

## 🌿 Branches

### 📌 Regra Geral
- Sempre crie uma nova branch baseada na `dev`.
- Nome da branch deve seguir o padrão: `feature/nome-da-tarefa`.

### 🧪 Exemplos de nomes:
- `feature/cadastro-animal`
- `feature/login-usuario`
- `feature/listagem-voluntarios`

---

## 💬 Commits

Use mensagens claras e diretas com prefixos padronizados:

| Prefixo | Descrição                        |
|---------|----------------------------------|
| `feat`  | Nova funcionalidade              |
| `fix`   | Correção de bug                  |
| `style` | Alterações visuais ou de layout  |
| `refactor` | Refatoração sem alterar comportamento |
| `docs`  | Documentação                     |

### ✔️ Exemplo:
```
feat: adiciona formulário de voluntariado
fix: corrige erro ao salvar novo pet
```

---

## 🔁 Pull Requests

1. Após finalizar a tarefa, **abra um Pull Request para a `dev`**.
2. Peça para alguém da equipe revisar e aprovar.
3. Após o merge, **exclua a branch** para manter o repositório limpo.

---

## 🚫 Atenção!

- **Nunca** commite ou faça push direto nas branches `main` ou `dev`.
- Tudo deve passar por **Pull Request**.

---

## 📎 Sugestão de Fluxo
```bash
git checkout dev
git pull
git checkout -b feature/nome-da-tarefa

# ...faz alterações...

git add .
git commit -m "feat: descrição do que foi feito"
git push origin feature/nome-da-tarefa
```

Depois, vá até o GitHub e crie o Pull Request para `dev`.

---
