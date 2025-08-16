export const CategoryComponent = (function () {
    const module = {};

    module.categoryItemCreate = () =>
        `<div class="col-md-6 col-lg-4 mb-3 category-item-create">
            <div class="card h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between gap-3 with-validation">
                        <input type="text" class="category-title-input form-control form-control-sm" name="title"/>
                        <div class="btn-group">
                            <button class="btn btn-sm btn-success save-category-btn" type="button">
                                <i class="fas fa-check"></i>
                            </button>
                            <button class="btn btn-sm btn-danger cancel-category-btn" type="button">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>`

    module.categoryItemDetail = (categoryDetail) =>
        `<div class="col-md-6 col-lg-4 mb-3 category-item-detail">
            <div class="card h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between gap-3 align-items-start mb-2 with-validation">
                        <h5 class="category-title-text card-title mb-0">${categoryDetail.title}</h5>
                        <input class="category-title-input form-control form-control-sm m-auto" 
                                type="text" value="${categoryDetail.title}" name="title" hidden>
                        <input class="category-id-input" type="hidden" value="${categoryDetail.categoryId}">
                        <div class="btn-group confirm-update-area" hidden>
                            <button class="btn btn-sm btn-success update-category-btn" type="button">
                                <i class="fas fa-check"></i>
                            </button>
                            <button class="btn btn-sm btn-danger cancel-update-category-btn" type="button">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>
                        <div class="dropdown">
                            <button class="btn btn-sm btn-outline-secondary" type="button"
                                    data-bs-toggle="dropdown">
                                <i class="fas fa-ellipsis-v"></i>
                            </button>
                            <ul class="dropdown-menu">
                                <li>
                                    <button type="button" class="edit-category-btn dropdown-item">
                                        <i class="fas fa-pen me-2"></i>
                                        <span>${editTitle}</span>
                                    </button>
                                </li>
                                <li>
                                    <button type="button" class="delete-category-btn dropdown-item text-danger">
                                        <i class="fas fa-trash-alt me-2"></i>
                                        <span>${deleteTitle}</span>
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>`

    return module;
})();