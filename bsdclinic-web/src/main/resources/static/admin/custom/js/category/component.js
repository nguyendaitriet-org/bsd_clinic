export const CategoryComponent = (function () {
    const module = {};

    module.categoryItemCreate = () =>
        `<div class="col-md-6 col-lg-4 mb-3 category-item-create">
            <div class="card h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between gap-3">
                        <input type="text" class="category-title form-control form-control-sm"/>
                        <div class="btn-group">
                            <button class="btn btn-sm btn-danger cancel-category-btn" type="button">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>`

    return module;
})();