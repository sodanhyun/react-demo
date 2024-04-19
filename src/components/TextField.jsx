export function TextField({label, type, ...props}) {

    return(
        <div>
            <label>{label}
                <input type={type} {...props}/>
            </label>
        </div>
    )
}