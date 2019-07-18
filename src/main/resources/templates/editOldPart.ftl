<#import "partsCode/common.ftl" as c>
<@c.page>
    <div class="container">
        Редактор для детали "${part.id}"
        <form class="form-horizontal" action="/update" method="post">
            <tbody>
            <tr>
                <input type="hidden" value="${part.id}" name="partId">
            </tr>
            <tr>
                Название <label>
                    <input type="text" value="${part.title}" name="partTitle">
                </label>
            </tr>
            <tr>
                Количество <label>
                    <input type="number" value="${part.quantity}" name="partQuantity">
                </label>
            </tr>
            <tr>
                Деталь необходима для сборки?<label>
                    <input type="checkbox"   name="partRequired">
                </label>
            </tr>
            </tbody>


            <button type="submit">Сохранить</button>
        </form>
    </div>

</@c.page>